package ru.svolf.convertx.ui.activity

import android.os.Bundle
import android.os.Process
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.mikepenz.fastadapter.ClickListener
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import ru.svolf.convertx.R
import ru.svolf.convertx.databinding.ActivityMainBinding
import ru.svolf.convertx.ui.fragments.base.MainMenuItem
import kotlin.system.exitProcess

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    private val navHostFragment: Fragment? by lazy { supportFragmentManager.findFragmentById(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        //supportActionBar.setHomeAsUpIndicator(R.drawable.ic_list);
        initNavigation()
        initMenu()
    }

    private fun initNavigation() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navHostFragment?.childFragmentManager?.backStackEntryCount == 0) {
                    finish()
                } else {
                    navController.navigateUp()
                    binding.backdrop.update(false)
                }
            }
        })
    }

    private fun initMenu(){
        val itemAdapter = ItemAdapter<MainMenuItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        binding.backdropMenu.adapter = fastAdapter

        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val data = viewModel.getData()
        data.observe(this) { items ->
            itemAdapter.add(items)
        }

        fastAdapter.onClickListener = object: ClickListener<MainMenuItem> {
            override fun invoke(v: View?, adapter: IAdapter<MainMenuItem>, item: MainMenuItem, position: Int): Boolean {
                binding.backdrop.close()
                when(item.id) {
                    1 -> {
                        navController.navigate(R.id.unicodeFragment)
                        binding.backdrop.close()
                    }
                    2 -> {
                        navController.navigate(R.id.base64Fragment)
                        binding.backdrop.close()
                    }
                    3 -> {
                        navController.navigate(R.id.hexFragment)
                        binding.backdrop.close()
                    }
                    4 -> {
                        navController.navigate(R.id.regexDragonFragment)
                        binding.backdrop.close()
                    }
                    5 -> {
                        navController.navigate(R.id.paletteActivity)
                        binding.backdrop.close()
                    }
                    6 -> {
                        navController.navigate(R.id.listCoders)
                        binding.backdrop.close()
                    }
                    7 -> {
                        navController.navigate(R.id.aboutFragment)
                        binding.backdrop.close()
                    }
                    8 -> {
                        navController.navigate(R.id.settingsFragment)
                        binding.backdrop.close()
                    }
                    9 -> showExitDialog()
                }
                return false
            }
        }
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setMessage(R.string.dr_close_app)
            .setPositiveButton(R.string.yes) { _, _ ->
                Process.killProcess(Process.myPid())
                exitProcess(0)
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    companion object {
        var press_time = 0L
    }

}
