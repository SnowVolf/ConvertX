package ru.SnowVolf.convertx;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import ru.SnowVolf.convertx.fragments.Base64Fragment;
import ru.SnowVolf.convertx.fragments.HexFragment;
import ru.SnowVolf.convertx.fragments.RgbFragment;
import ru.SnowVolf.convertx.fragments.UnicodeFragment;
import ru.SnowVolf.convertx.fragments.extended.ListCoders;
import ru.SnowVolf.convertx.other.AboutActivity;
import ru.SnowVolf.convertx.palette.PaletteActivity;
import ru.SnowVolf.convertx.regexdragon.RegexDragon;
import ru.SnowVolf.convertx.settings.SettingsActivity;
import ru.SnowVolf.convertx.utils.SystemF;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences preferences;
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        if (preferences.getBoolean("Theme.Theme", true)) {
            preferences.edit().putBoolean("Theme.Theme", true).apply();
            SystemF.onActivityCreateSetTheme(this);
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.AppTheme_Dark);
            preferences.edit().putBoolean("Theme.Theme", false).apply();
        }
        //иначе будет падать на KK и ниже
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new UnicodeFragment()).commit();
        //noinspection ConstantConditions
        getSupportActionBar().setSubtitle(getString(R.string.dr_unicode));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dr_unicode);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Подключаем и настраиваем слушателя NavigationDrawer
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //скрываем клавиатуру при открытиии drawer-a
                InputMethodManager iMM = (InputMethodManager) MainActivity.this.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
                //noinspection ConstantConditions
                iMM.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                /*//drawer закрыт, показываем клавиатуру
                InputMethodManager iMM = (InputMethodManager) MainActivity.this.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
                iMM.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*/
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.unicode_dec);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            //getSupportFragmentManager().popBackStack();
        } else {
            //getSupportFragmentManager().popBackStack();
            super.onBackPressed();
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.unicode_dec) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dr_unicode);
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, new UnicodeFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    //.addToBackStack(null)
                    .commit();
            getSupportFragmentManager().popBackStack();
            getSupportActionBar().setSubtitle(R.string.dr_unicode);
        } else if (id == R.id.base64_dec) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dr_base64);
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, new Base64Fragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    //.addToBackStack(null)
                    .commit();
            getSupportActionBar().setSubtitle(getString(R.string.dr_base64));
        } else if (id == R.id.hex_dec) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dr_hex);
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, new HexFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    //.addToBackStack(null)
                    .commit();
            getSupportActionBar().setSubtitle(getString(R.string.dr_hex));
        } else if (id == R.id.rgb_dec) {
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager()
                    .beginTransaction().replace(R.id.frame_container, new RgbFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    //.addToBackStack(null)
                    .commit();
            getSupportActionBar().setSubtitle(getString(R.string.dr_rgb));
        } else if (id == R.id.regex_dragon){
            Intent dragon = new Intent(this, RegexDragon.class);
            startActivity(dragon);
        } else if (id == R.id.hex_palette){
            Intent palette = new Intent(this, PaletteActivity.class);
            startActivity(palette);
        } else if (id == R.id.other_dec) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dr_other_coders);
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().popBackStack();
            //getFragmentManager().popBackStackImmediate();
            getSupportFragmentManager()
                    .beginTransaction().replace(R.id.frame_container, new ListCoders())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    //.addToBackStack(null)
                    .commit();
            getSupportActionBar().setSubtitle(getString(R.string.dr_other));
        } else if (id == R.id.settings_act) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
        } else if (id == R.id.nav_about) {
            Intent about = new Intent(this, AboutActivity.class);
            startActivity(about);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onDestroy(){
        super.onDestroy();
    }
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

    }
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        if (preferences.getBoolean("Theme.Theme", true)) {
            preferences.edit().putBoolean("Theme.Theme", true).apply();
            SystemF.onActivityCreateSetTheme(this);
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.AppTheme_Dark);
            preferences.edit().putBoolean("Theme.Theme", false).apply();
        }
    }
    protected void onResume() {
        super.onResume();
        if (preferences.getBoolean("Theme.Theme", true)) {
            preferences.edit().putBoolean("Theme.Theme", true).apply();
            SystemF.onActivityCreateSetTheme(this);
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.AppTheme_Dark);
            preferences.edit().putBoolean("Theme.Theme", false).apply();
        }
    }

}
