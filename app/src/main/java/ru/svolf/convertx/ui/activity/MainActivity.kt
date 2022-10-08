package ru.svolf.convertx.ui.activity

import android.os.Bundle
import android.os.Process
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.mikepenz.fastadapter.ClickListener
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

import ru.svolf.convertx.R
import ru.svolf.convertx.databinding.ActivityMainBinding
import ru.svolf.convertx.settings.Preferences
import ru.svolf.convertx.ui.Toasty
import ru.svolf.convertx.ui.fragments.base.MainMenuItem

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        initNavigation()
        initMenu()
    }

    private fun initNavigation() {
      
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigateUp()
                binding.backdrop.update(false)
            }
        })
    }

    private fun initMenu(){
        val itemAdapter = ItemAdapter<MainMenuItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)

        binding.backdropMenu.adapter = fastAdapter
        itemAdapter.add(
            MainMenuItem(1, R.drawable.ic_dr_unicode, R.string.dr_unicode),
            MainMenuItem(2, R.drawable.ic_dr_base64, R.string.dr_base64),
            MainMenuItem(3, R.drawable.ic_dr_hex, R.string.dr_hex),
            MainMenuItem(4, R.drawable.ic_dr_regexdragon, R.string.dr_regex_dragon),
            MainMenuItem(5, R.drawable.ic_dr_palette, R.string.dr_hex_palette),
            MainMenuItem(6, R.drawable.ic_dr_other_coders, R.string.dr_other1),
            MainMenuItem(7, R.drawable.ic_info, R.string.dr_about),
            MainMenuItem(8, R.drawable.ic_dr_settings, R.string.settings),
            MainMenuItem(9, R.drawable.ic_action_arrow_back, R.string.dr_close_app)
        )
        fastAdapter.onClickListener = object: ClickListener<MainMenuItem> {
            override fun invoke(v: View?, adapter: IAdapter<MainMenuItem>, item: MainMenuItem, position: Int): Boolean {
                binding.backdrop.close();
                when(item.id) {
                    1 -> {
                        navController.navigate(R.id.unicodeFragment)
                        binding.backdrop.close();
                    }
                    2 -> {
                        navController.navigate(R.id.base64Fragment)
                        binding.backdrop.close();
                    }
                    3 -> {
                        navController.navigate(R.id.hexFragment)
                        binding.backdrop.close();
                    }
                    4 -> {
                        navController.navigate(R.id.regexDragonFragment)
                        binding.backdrop.close();
                    }
                    5 -> {
                        navController.navigate(R.id.paletteActivity)
                        binding.backdrop.close();
                    }
                    6 -> {
                        navController.navigate(R.id.listCoders)
                        binding.backdrop.close();
                    }
                    7 -> {
                        navController.navigate(R.id.aboutFragment)
                        binding.backdrop.close();
                    }
                    8 -> {
                        navController.navigate(R.id.settingsFragment)
                        binding.backdrop.close();
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
            .setPositiveButton(R.string.yes) { d, i ->
                Process.killProcess(Process.myPid())
                System.exit(0)
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    companion object {
        var press_time = 0L
    }

}
