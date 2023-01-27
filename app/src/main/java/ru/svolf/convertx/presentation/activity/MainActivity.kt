package ru.svolf.convertx.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mikepenz.fastadapter.ClickListener
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import ru.svolf.convertx.R
import ru.svolf.convertx.databinding.ActivityMainBinding
import ru.svolf.convertx.presentation.fragments.base.MainMenuItem
import ru.svolf.convertx.settings.Preferences
import kotlin.system.exitProcess

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    private val navController: NavController by lazy { Navigation.findNavController(this, R.id.nav_host_fragment) }
    private val navHostFragment: Fragment? by lazy { supportFragmentManager.findFragmentById(R.id.nav_host_fragment) }

    companion object {
        var press_time = 0L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        initNavigation()
        initMenu()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val pkg = packageName
        if (intent != null){
            when (intent.action) {
                pkg.plus(".ACTION_UNICODE") -> navController.navigate(R.id.unicodeFragment)
                pkg.plus(".ACTION_BASE64") -> navController.navigate(R.id.base64Fragment)
                pkg.plus(".ACTION_HEX") -> navController.navigate(R.id.hexFragment)
                pkg.plus(".ACTION_REGEX") -> navController.navigate(R.id.regexDragonFragment)
                pkg.plus(".ACTION_PALETTE") -> navController.navigate(R.id.paletteFragment)
                else -> {
                    Toast.makeText(this, "Missing action, please update ConvertX to latest version", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initNavigation() {
        NavigationUI.setupActionBarWithNavController(this, navController)
        navController.navigate(R.id.unicodeFragment)

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // костыль для NavigationUI
                if (navHostFragment?.childFragmentManager?.backStackEntryCount == 1) {
                    if (Preferences.isTwiceBackAllowed and (press_time + 2000 < System.currentTimeMillis()) ) {
                        press_time = System.currentTimeMillis()
                        Toast.makeText(this@MainActivity, getString(R.string.press_back_once_more), Toast.LENGTH_SHORT).show()
                    } else finish()
                } else {
                    navController.navigateUp()
                }
            }
        })
    }

    private fun initMenu(){
        val itemAdapter = ItemAdapter<MainMenuItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        binding.backdropMenu.adapter = fastAdapter

        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getData().observe(this) { items ->
            itemAdapter.add(items)
        }

        fastAdapter.onClickListener = object: ClickListener<MainMenuItem> {
            override fun invoke(v: View?, adapter: IAdapter<MainMenuItem>, item: MainMenuItem, position: Int): Boolean {
                binding.backdrop.close()
                binding.backdrop.menuIcon = R.drawable.menu
                // Exit button
                if (item.id == android.R.id.home) {
                    showExitDialog()
                } else {
                    navController.navigate(item.id)
                }
                return false
            }
        }
    }

    private fun showExitDialog() {
        MaterialAlertDialogBuilder(this)
            .setMessage(R.string.dr_close_app)
            .setPositiveButton(R.string.yes) { _, _ ->
                Process.killProcess(Process.myPid())
                exitProcess(0)
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

}
