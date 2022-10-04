package ru.SnowVolf.convertx.ui.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Process
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.Drawer.OnDrawerListener
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import ru.SnowVolf.convertx.R
import ru.SnowVolf.convertx.palette.PaletteActivity
import ru.SnowVolf.convertx.regexdragon.RegexDragonFragment
import ru.SnowVolf.convertx.settings.DefStrings.GIRL
import ru.SnowVolf.convertx.settings.DefStrings.TAG
import ru.SnowVolf.convertx.settings.Preferences
import ru.SnowVolf.convertx.settings.SettingsActivity
import ru.SnowVolf.convertx.settings.SettingsFragment
import ru.SnowVolf.convertx.ui.Interfacer
import ru.SnowVolf.convertx.ui.Toasty
import ru.SnowVolf.convertx.ui.fragments.extra.AboutFragment
import ru.SnowVolf.convertx.ui.fragments.extra.ListCoders
import ru.SnowVolf.convertx.ui.fragments.main.Base64Fragment
import ru.SnowVolf.convertx.ui.fragments.main.FontsTesterFragment
import ru.SnowVolf.convertx.ui.fragments.main.HexFragment
import ru.SnowVolf.convertx.ui.fragments.main.UnicodeFragment

class MainActivity : BaseActivity() {
    /**
     * Логгируется в 2-х потоках
     * JavaGirl -- Helper поток (тег GIRL)
     * ConvertX -- Основной поток (тег TAG)
     */
    var textColor = 0
    var toolbar: Toolbar? = null
    lateinit var result: Drawer
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "MainActivity")
        Log.w(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        //иначе будет падать на KK и ниже
        Log.i(GIRL, "VectorCompat : Disabled")
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        Log.i(GIRL, "VectorCompat : Enabled")
        PreferenceManager.setDefaultValues(this, R.xml.setting, false)
        setContentView(R.layout.activity_main)
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        Log.i(TAG, "Set Toolbar instead ActionBar")
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, UnicodeFragment()).commit()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        textColor = if (SettingsFragment.getThemeIndex(this) == Interfacer.Theme.LIGHT.ordinal) {
            Color.BLACK
        } else Color.WHITE
        //декларируем пункты
        //если возвращать true, дровер не будет закрываться автоматически
        Log.i(TAG, "OnAdd DrawerItem : 1")
        val d1 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_unicode)
        val unicode = PrimaryDrawerItem()
            .withIdentifier(1)
            .withName(R.string.dr_unicode)
            .withIcon(d1)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                Log.i(TAG, "StartFragment : Unicode")
                toolbar!!.visibility = View.VISIBLE
                supportFragmentManager.popBackStack()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, UnicodeFragment())
                    .commit()
                supportFragmentManager.popBackStack()
                Preferences.drawerPosition = position
                false
            }
        Log.i(TAG, "OnAdd DrawerItem : 2")
        val d2 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_base64)
        val base = PrimaryDrawerItem()
            .withIdentifier(2)
            .withName(R.string.dr_base64)
            .withIcon(d2)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                Log.i(TAG, "StartFragment : Base64")
                toolbar!!.visibility = View.VISIBLE
                supportFragmentManager.popBackStack()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, Base64Fragment())
                    .commit()
                Preferences.drawerPosition = position
                false
            }
        Log.i(TAG, "OnAdd DrawerItem : 3")
        val d3 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_hex)
        val hex = PrimaryDrawerItem()
            .withIdentifier(3)
            .withName(R.string.dr_hex)
            .withIcon(d3)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                Log.i(TAG, "StartFragment : Hex")
                toolbar!!.visibility = View.VISIBLE
                supportFragmentManager.popBackStack()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, HexFragment())
                    .commit()
                Preferences.drawerPosition = position
                false
            }
        Log.i(TAG, "OnAdd DrawerItem : 4")
        val d4 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_palette)
        val palette = PrimaryDrawerItem()
            .withIdentifier(4)
            .withName(R.string.dr_hex_palette)
            .withSelectable(false)
            .withIcon(d4)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                val palette1 = Intent(this@MainActivity, PaletteActivity::class.java)
                startActivity(palette1)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                Log.i(TAG, "StartActivity : Palette")
                false
            }
        val d45 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_fonts_tester)
        val fontsTester = PrimaryDrawerItem()
            .withIdentifier(5)
            .withName(R.string.dr_fonts_tester)
            .withIcon(d45)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                Log.i(TAG, "StartFragment : FontsTester")
                toolbar!!.visibility = View.VISIBLE
                supportFragmentManager.popBackStack()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, FontsTesterFragment())
                    .commit()
                Preferences.drawerPosition = position
                false
            }
        Log.i(TAG, "OnAdd DrawerItem : 5")
        val d5 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_regexdragon)
        val regex = PrimaryDrawerItem()
            .withIdentifier(6)
            .withName(R.string.dr_regex_dragon)
            .withIcon(d5)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                try {
                    supportFragmentManager.popBackStack()
                    supportFragmentManager
                        .beginTransaction().replace(R.id.frame_container, RegexDragonFragment())
                        .commit()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                Log.i(TAG, "StartFragment : Dragon")
                Preferences.drawerPosition = position
                false
            }
        Log.i(TAG, "OnAdd DrawerItem : 6")
        val d6 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_other_coders)
        val other = PrimaryDrawerItem()
            .withIdentifier(7)
            .withName(R.string.dr_other1)
            .withIcon(d6)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                Log.i(TAG, "StartFragment : Other Tools")
                toolbar!!.visibility = View.VISIBLE
                supportFragmentManager.popBackStack()
                supportFragmentManager
                    .beginTransaction().replace(R.id.frame_container, ListCoders())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                Preferences.drawerPosition = position
                false
            }
        val secondaryOther = SecondaryDrawerItem()
            .withName(R.string.other)
            .withSelectable(false)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? -> true }
        Log.i(TAG, "OnAdd DrawerItem : 7")
        val d7 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_settings)
        val settings = PrimaryDrawerItem()
            .withIdentifier(8)
            .withName(R.string.settings)
            .withSelectable(false)
            .withIcon(d7)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                true
            }
        Log.i(TAG, "OnAdd DrawerItem : 8")
        val d8 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_about)
        val about: PrimaryDrawerItem = PrimaryDrawerItem()
            .withIdentifier(9)
            .withName(R.string.about_program)
            .withDescription(getBuildName(this))
            .withIcon(d8)
            .withOnDrawerItemClickListener(Drawer.OnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                Log.i(TAG, "StartFragment : About")
                supportFragmentManager.popBackStack()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, AboutFragment())
                    .addToBackStack(null)
                    .commit()
                Preferences.drawerPosition = position
                false
            })
        Log.i(TAG, "OnAdd DrawerItem : 9")
        val d9 = AppCompatResources.getDrawable(this, R.drawable.ic_exit)
        val kill = PrimaryDrawerItem()
            .withIdentifier(10)
            .withName(R.string.dr_close_app)
            .withIcon(d9)
            .withSelectable(false)
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? ->
                Log.i(TAG, "show Exit clicked")
                showExitDialog()
                true
            }

        //header вверху
        Log.w(TAG, "InitHeader")
        val lobster = Typeface.createFromAsset(assets, "fonts/3952.ttf")
        val header = AccountHeaderBuilder().withActivity(this)
            .addProfiles(
                ProfileDrawerItem()
                    .withName("Snow Volf").withIcon(R.drawable.girlava).withEmail("svolf15@yandex.ru")
            ).withSelectionListEnabledForSingleProfile(false).withCompactStyle(true).withTextColor(textColor).withTypeface(lobster).build()


        //инициализируем drawer
        Log.w(TAG, "InitDrawer")
        result = DrawerBuilder()
            .withActionBarDrawerToggle(true)
            .withActionBarDrawerToggleAnimated(true)
            .withActivity(this)
            .withAccountHeader(header)
            .withSavedInstance(savedInstanceState)
            .withToolbar(toolbar!!) //Подключаем и настраиваем слушателя NavigationDrawer
            .withOnDrawerListener(object : OnDrawerListener {
                override fun onDrawerOpened(drawerView: View) {
                    //скрываем клавиатуру при открытиии drawer-a
                    val iMM = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    iMM.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                }

                override fun onDrawerClosed(drawerView: View) {}
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            })
            .addDrawerItems(
                unicode,
                base,
                hex,
                palette,
                fontsTester,
                regex,
                other,
                DividerDrawerItem(),
                secondaryOther,
                settings,
                about,
                kill
            ).build()
        result.setSelection(preferences?.getInt("drawer.position", 1)!!.toLong())
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

    override fun onDestroy() {
        Log.w(TAG, "OnDestroy")
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.w(TAG, "OnSaveInstance")
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.w(TAG, "OnRestoreInstance")
        super.onRestoreInstanceState(savedInstanceState)
    }

    fun setToolbarSubtitle(subtitle: String?) {
        toolbar!!.subtitle = subtitle
    }

    fun showExitDialog() {
        Log.i(GIRL, "show Exit")
        MaterialDialog.Builder(this)
            .content(R.string.dr_close_app)
            .positiveText(R.string.yes)
            .onPositive { dialog: MaterialDialog?, which: DialogAction? ->
                Log.i(TAG, "show Exit : positive")
                Process.killProcess(Process.myPid())
                System.exit(0)
            }
            .negativeText(R.string.no)
            .show()
    }

    fun getBuildName(context: Context): String {
        var programBuild = "" //context.getString(R.string.app_name);
        try {
            val pkg = context.packageName
            val pkgInfo = context.packageManager.getPackageInfo(pkg, PackageManager.GET_META_DATA)
            programBuild += " v." + pkgInfo.versionName
        } catch (e1: PackageManager.NameNotFoundException) {
            e1.printStackTrace()
        }
        return programBuild
    }

    companion object {
        private var press_time = System.currentTimeMillis()
    }
}