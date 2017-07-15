package ru.SnowVolf.convertx.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.other.Extras;
import ru.SnowVolf.convertx.palette.PaletteActivity;
import ru.SnowVolf.convertx.regexdragon.RegexDragonFragment;
import ru.SnowVolf.convertx.settings.DefStrings;
import ru.SnowVolf.convertx.settings.Preferences;
import ru.SnowVolf.convertx.settings.SettingsActivity;
import ru.SnowVolf.convertx.settings.SettingsFragment;
import ru.SnowVolf.convertx.ui.Interfacer;
import ru.SnowVolf.convertx.ui.Toasty;
import ru.SnowVolf.convertx.ui.fragments.extra.AboutFragment;
import ru.SnowVolf.convertx.ui.fragments.extra.ListCoders;
import ru.SnowVolf.convertx.ui.fragments.main.Base64Fragment;
import ru.SnowVolf.convertx.ui.fragments.main.FontsTesterFragment;
import ru.SnowVolf.convertx.ui.fragments.main.HexFragment;
import ru.SnowVolf.convertx.ui.fragments.main.UnicodeFragment;


public class MainActivity extends BaseActivity {
    /**
     * Логгируется в 2-х потоках
     * JavaGirl -- Helper поток (тег GIRL)
     * ConvertX -- Основной поток (тег TAG)
     */
    int textColor;
    Toolbar toolbar;
    Drawer result;
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(DefStrings.INSTANCE.getTAG(), "MainActivity");
        Log.w(DefStrings.INSTANCE.getTAG(), "onCreate");
        super.onCreate(savedInstanceState);
        //иначе будет падать на KK и ниже
        Log.i(DefStrings.INSTANCE.getGIRL(), "VectorCompat : Disabled");
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Log.i(DefStrings.INSTANCE.getGIRL(), "VectorCompat : Enabled");
        PreferenceManager.setDefaultValues(this, R.xml.setting, false);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.i(DefStrings.INSTANCE.getTAG(), "Set Toolbar instead ActionBar");
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new UnicodeFragment()).commit();
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (SettingsFragment.getThemeIndex(this) == Interfacer.Theme.LIGHT.ordinal()){
            textColor = Color.BLACK;
        } else textColor = Color.WHITE;
        //декларируем пункты
        //если возвращать true, дровер не будет закрываться автоматически
        Log.i(DefStrings.INSTANCE.getTAG(), "OnAdd DrawerItem : 1");
        Drawable d1 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_unicode);
        PrimaryDrawerItem unicode = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.dr_unicode)
                .withIcon(d1)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    Log.i(DefStrings.INSTANCE.getTAG(), "StartFragment : Unicode");
                    toolbar.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_container, new UnicodeFragment())
                            .commit();
                    getSupportFragmentManager().popBackStack();
                    Preferences.setDrawerPosition(position);
                    return false;
                });
        Log.i(DefStrings.INSTANCE.getTAG(), "OnAdd DrawerItem : 2");
        Drawable d2 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_base64);
        PrimaryDrawerItem base = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName(R.string.dr_base64)
                .withIcon(d2)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    Log.i(DefStrings.INSTANCE.getTAG(), "StartFragment : Base64");
                    toolbar.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_container, new Base64Fragment())
                            .commit();
                    Preferences.setDrawerPosition(position);
                    return false;
                });
        Log.i(DefStrings.INSTANCE.getTAG(), "OnAdd DrawerItem : 3");
        Drawable d3 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_hex);
        PrimaryDrawerItem hex = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName(R.string.dr_hex)
                .withIcon(d3)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    Log.i(DefStrings.INSTANCE.getTAG(), "StartFragment : Hex");
                    toolbar.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_container, new HexFragment())
                            .commit();
                    Preferences.setDrawerPosition(position);
                    return false;
                });
        Log.i(DefStrings.INSTANCE.getTAG(), "OnAdd DrawerItem : 4");
        Drawable d4 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_palette);
        PrimaryDrawerItem palette = new PrimaryDrawerItem()
                .withIdentifier(4)
                .withName(R.string.dr_hex_palette)
                .withSelectable(false)
                .withIcon(d4)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    Intent palette1 = new Intent(MainActivity.this, PaletteActivity.class);
                    startActivity(palette1);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    Log.i(DefStrings.INSTANCE.getTAG(), "StartActivity : Palette");
                    return false;
                });
        Drawable d45 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_fonts_tester);
        PrimaryDrawerItem fontsTester = new PrimaryDrawerItem()
                .withIdentifier(5)
                .withName(R.string.dr_fonts_tester)
                .withIcon(d45)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    Log.i(DefStrings.INSTANCE.getTAG(), "StartFragment : FontsTester");
                    toolbar.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_container, new FontsTesterFragment())
                            .commit();
                    Preferences.setDrawerPosition(position);
                    return false;
                });
        Log.i(DefStrings.INSTANCE.getTAG(), "OnAdd DrawerItem : 5");
        Drawable d5 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_regexdragon);
        PrimaryDrawerItem regex = new PrimaryDrawerItem()
                .withIdentifier(6)
                .withName(R.string.dr_regex_dragon)
                .withIcon(d5)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    try {
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager()
                                .beginTransaction().replace(R.id.frame_container, new RegexDragonFragment())
                                .commit();
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }
                    Log.i(DefStrings.INSTANCE.getTAG(), "StartFragment : Dragon");
                    Preferences.setDrawerPosition(position);
                    return false;
                });
        Log.i(DefStrings.INSTANCE.getTAG(), "OnAdd DrawerItem : 6");
        Drawable d6 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_other_coders);
        PrimaryDrawerItem other = new PrimaryDrawerItem()
                .withIdentifier(7)
                .withName(R.string.dr_other1)
                .withIcon(d6)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    Log.i(DefStrings.INSTANCE.getTAG(), "StartFragment : Other Tools");
                    toolbar.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager()
                            .beginTransaction().replace(R.id.frame_container, new ListCoders())
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                    Preferences.setDrawerPosition(position);
                    return false;
                });
        SecondaryDrawerItem secondaryOther = new SecondaryDrawerItem()
                .withName(R.string.other)
                .withSelectable(false)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> true);

        Log.i(DefStrings.INSTANCE.getTAG(), "OnAdd DrawerItem : 7");
        Drawable d7 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_settings);
        PrimaryDrawerItem settings = new PrimaryDrawerItem()
                .withIdentifier(8)
                .withName(R.string.settings)
                .withSelectable(false)
                .withIcon(d7)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    return true;
                });

        Log.i(DefStrings.INSTANCE.getTAG(), "OnAdd DrawerItem : 8");
        Drawable d8 = AppCompatResources.getDrawable(this, R.drawable.ic_dr_about);
        PrimaryDrawerItem about = new PrimaryDrawerItem()
                .withIdentifier(9)
                .withName(R.string.about_program)
                .withDescription(Extras.getBuildName(this))
                .withIcon(d8)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    Log.i(DefStrings.INSTANCE.getTAG(), "StartFragment : About");
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_container, new AboutFragment())
                            .addToBackStack(null)
                            .commit();
                    Preferences.setDrawerPosition(position);
                    return false;
                });
        Log.i(DefStrings.INSTANCE.getTAG(), "OnAdd DrawerItem : 9");
        Drawable d9 = AppCompatResources.getDrawable(this, R.drawable.ic_exit);
        PrimaryDrawerItem kill = new PrimaryDrawerItem()
                .withIdentifier(10)
                .withName(R.string.dr_close_app)
                .withIcon(d9)
                .withSelectable(false)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    Log.i(DefStrings.INSTANCE.getTAG(), "show Exit clicked");
                    showExitDialog();
                    return true;
                });

        //header вверху
        Log.w(DefStrings.INSTANCE.getTAG(), "InitHeader");

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/3952.ttf");
        AccountHeader header = new AccountHeaderBuilder().withActivity(this)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Snow Volf").withIcon(R.drawable.girlava).withEmail("svolf15@yandex.ru")
                ).withSelectionListEnabledForSingleProfile(false).withCompactStyle(true).withTextColor(textColor).withTypeface(lobster).build();


        //инициализируем drawer
        Log.w(DefStrings.INSTANCE.getTAG(), "InitDrawer");
        result = new DrawerBuilder()
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withActivity(this)
                .withAccountHeader(header)
                .withSavedInstance(savedInstanceState)
                .withToolbar(toolbar)
                //Подключаем и настраиваем слушателя NavigationDrawer
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        //скрываем клавиатуру при открытиии drawer-a
                        InputMethodManager iMM = (InputMethodManager) getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
                        //noinspection ConstantConditions
                        iMM.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }
                })
                .addDrawerItems(
                        unicode,
                        base,
                        hex,
                        palette,
                        fontsTester,
                        regex,
                        other,
                        new DividerDrawerItem(),
                        secondaryOther,
                        settings,
                        about,
                        kill
                ).build();
        result.setSelection(preferences.getInt("drawer.position", 1));
    }

    private static long press_time = System.currentTimeMillis();
    @Override
    public void onBackPressed() {
        if (result.isDrawerOpen()){
            result.closeDrawer();
        } else if (Preferences.isTwiceBackAllowed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                if (press_time + 2000 > System.currentTimeMillis()) {
                    finish();
                } else {
                    Toasty.info(this, getString(R.string.press_back_once_more), Toast.LENGTH_SHORT, true).show();
                    press_time = System.currentTimeMillis();
                }
            } else {
                super.onBackPressed();
            }
        } else super.onBackPressed();
    }

    protected void onDestroy(){
        Log.w(DefStrings.INSTANCE.getTAG(), "OnDestroy");
        super.onDestroy();
    }

    protected void onSaveInstanceState(Bundle outState){
        Log.w(DefStrings.INSTANCE.getTAG(), "OnSaveInstance");
        super.onSaveInstanceState(outState);

    }
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        Log.w(DefStrings.INSTANCE.getTAG(), "OnRestoreInstance");
        super.onRestoreInstanceState(savedInstanceState);

    }

    public void setToolbarSubtitle(String subtitle){
        toolbar.setSubtitle(subtitle);
    }

    public void showExitDialog() {
        Log.i(DefStrings.INSTANCE.getGIRL(), "show Exit");
        new MaterialDialog.Builder(this)
                .content(R.string.dr_close_app)
                .positiveText(R.string.yes)
                .onPositive((dialog, which) -> {
                    Log.i(DefStrings.INSTANCE.getTAG(), "show Exit : positive");
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                })
                .negativeText(R.string.no)
                .show();
    }
}