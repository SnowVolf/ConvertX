package ru.svolf.convertx.ui.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.preference.PreferenceManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.Drawer.OnDrawerListener
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import ru.svolf.convertx.BuildConfig
import ru.svolf.convertx.R
import ru.svolf.convertx.databinding.ActivityMainBinding
import ru.svolf.convertx.palette.PaletteActivity
import ru.svolf.convertx.regexdragon.RegexDragonFragment
import ru.svolf.convertx.settings.Preferences
import ru.svolf.convertx.settings.SettingsFragment
import ru.svolf.convertx.ui.Toasty
import ru.svolf.convertx.ui.fragments.extra.AboutFragment
import ru.svolf.convertx.ui.fragments.extra.ListCoders
import ru.svolf.convertx.ui.fragments.main.Base64Fragment
import ru.svolf.convertx.ui.fragments.main.HexFragment
import ru.svolf.convertx.ui.fragments.main.UnicodeFragment

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var result: Drawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBar.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val position = Preferences.drawerPosition.toLong()
        //декларируем пункты
        //если возвращать true, дровер не будет закрываться автоматически
        val unicode = PrimaryDrawerItem()
            .withIdentifier(1)
            .withName(R.string.dr_unicode)
            .withIcon(R.drawable.ic_dr_unicode)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                Preferences.drawerPosition = position
                transact(UnicodeFragment())
                false
            }

        val base = PrimaryDrawerItem()
            .withIdentifier(2)
            .withName(R.string.dr_base64)
            .withIcon(R.drawable.ic_dr_base64)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                Preferences.drawerPosition = position
                transact(Base64Fragment())
                false
            }

        val hex = PrimaryDrawerItem()
            .withIdentifier(3)
            .withName(R.string.dr_hex)
            .withIcon(R.drawable.ic_dr_hex)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                Preferences.drawerPosition = position
                transact(HexFragment())
                false
            }

        val palette = PrimaryDrawerItem()
            .withIdentifier(4)
            .withName(R.string.dr_hex_palette)
            .withSelectable(false)
            .withIcon(R.drawable.ic_dr_palette)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                val palette1 = Intent(this@MainActivity, PaletteActivity::class.java)
                startActivity(palette1)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                
                false
            }

        val regex = PrimaryDrawerItem()
            .withIdentifier(6)
            .withName(R.string.dr_regex_dragon)
            .withIcon(R.drawable.ic_dr_regexdragon)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                Preferences.drawerPosition = position
                transact(RegexDragonFragment())
                false
            }

        val other = PrimaryDrawerItem()
            .withIdentifier(7)
            .withName(R.string.dr_other1)
            .withIcon(R.drawable.ic_dr_other_coders)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                Preferences.drawerPosition = position
                transact(ListCoders())
                false
            }
        val secondaryOther = SecondaryDrawerItem()
            .withName(R.string.other)
            .withSelectable(false)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? -> true }

        val settings = PrimaryDrawerItem()
            .withIdentifier(8)
            .withName(R.string.settings)
            .withSelectable(false)
            .withIcon(R.drawable.ic_dr_settings)
            .withOnDrawerItemClickListener { _: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                Preferences.drawerPosition = position
                transact(SettingsFragment())
                false
            }

        val about: PrimaryDrawerItem = PrimaryDrawerItem()
            .withIdentifier(9)
            .withName(R.string.about_program)
            .withDescription(BuildConfig.VERSION_NAME)
            .withIcon(R.drawable.ic_dr_about)
            .withOnDrawerItemClickListener{ view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                Preferences.drawerPosition = position
                transact(AboutFragment())
                false
            }

        val kill = PrimaryDrawerItem()
            .withIdentifier(10)
            .withName(R.string.dr_close_app)
            .withIcon(R.drawable.ic_exit)
            .withSelectable(false)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                showExitDialog()
                true
            }

        //инициализируем drawer
        
        result = DrawerBuilder()
            .withActionBarDrawerToggle(true)
            .withActionBarDrawerToggleAnimated(true)
            .withActivity(this)
            .withSavedInstance(savedInstanceState)
            .withToolbar(binding.appBar.toolbar) //Подключаем и настраиваем слушателя NavigationDrawer
            .withOnDrawerListener(object : OnDrawerListener {
                override fun onDrawerOpened(drawerView: View) {
                    //скрываем клавиатуру при открытиии drawer-a
                    val iMM = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    iMM.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                }

                override fun onDrawerClosed(drawerView: View) {}
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            })
            .addDrawerItems(
                unicode,
                base,
                hex,
                palette,
                regex,
                other,
                DividerDrawerItem(),
                secondaryOther,
                settings,
                about,
                kill
            ).build()
        var h = Handler(Looper.getMainLooper())
        h.postDelayed(Runnable {
            result.setSelection(position)
        }, 1000)

    }

    override fun onBackPressed() {
        if (result.isDrawerOpen) {
            result.closeDrawer()
        } else if (Preferences.isTwiceBackAllowed) {
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (press_time + 2000 > System.currentTimeMillis()) {
                    finish()
                } else {
                    Toasty.info(this, getString(R.string.press_back_once_more), Toast.LENGTH_SHORT, true).show()
                    press_time = System.currentTimeMillis()
                }
            } else {
                super.onBackPressed()
            }
        } else super.onBackPressed()
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

    fun transact(fra: Fragment){
        var fragment = supportFragmentManager.findFragmentByTag(fra.javaClass.simpleName)
        if (fragment == null) {
            fragment = fra
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment, fra.javaClass.simpleName)
            .commit()
    }

    companion object {
        private var press_time = System.currentTimeMillis()
    }
}