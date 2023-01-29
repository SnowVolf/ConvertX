package ru.svolf.convertx.presentation.base

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
import ru.svolf.convertx.data.model.MainMenuVH
import ru.svolf.convertx.databinding.ActivityMainBinding
import ru.svolf.convertx.presentation.screens.settings.Preferences
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
        initNavigation()
        initMenu()
    }

    private fun initNavigation() {
        NavigationUI.setupActionBarWithNavController(this, navController, binding.backdrop)

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // костыль для NavigationUI
                if (navHostFragment?.childFragmentManager?.backStackEntryCount == 0) {
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
        val itemAdapter = ItemAdapter<MainMenuVH>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        binding.backdropMenu.adapter = fastAdapter

        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getData().observe(this) { items ->
            itemAdapter.set(items)
        }

        fastAdapter.onClickListener = object: ClickListener<MainMenuVH> {
            override fun invoke(v: View?, adapter: IAdapter<MainMenuVH>, item: MainMenuVH, position: Int): Boolean {
                // Иначе экран появится, но панель не закроется
                binding.backdrop.close()
                // Exit button
                if (item.id == android.R.id.home) {
                    showExitDialog()
                } else {
                    // Для того чтобы не было блуждания по экранам
                    navController.popBackStack()
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
