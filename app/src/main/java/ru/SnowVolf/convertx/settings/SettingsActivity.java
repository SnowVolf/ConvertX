package ru.SnowVolf.convertx.settings;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuItem;

import ru.SnowVolf.convertx.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //иначе будет падать на kit-kat и ниже
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeAsUpIndicator(R.drawable.ic_dr_settings);
            ab.setDisplayShowTitleEnabled(true);
            ab.isHideOnContentScrollEnabled();
        }
    }
    //лень было делать еще один menu-res
    //поэтому решил нарисовать menu программно. никто же не против?
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (menu != null){
            menu.clear();
        } else
            menu = new MenuBuilder(this);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
        }
    }
