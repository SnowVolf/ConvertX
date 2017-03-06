package ru.SnowVolf.convertx.regexdragon;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.utils.SystemF;

public class RegexDragon extends AppCompatActivity {
    SharedPreferences preferences;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] icTab = {
            R.drawable.ic_dr_regexdragon,
            R.drawable.ic_menu_spur,
            R.drawable.ic_toast_info_outline
    };

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
        setContentView(R.layout.activity_regex_dragon);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_dr_regexdragon);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        setIcons();
    }

    private void setIcons() {
        if (icTab != null) {
            tabLayout.getTabAt(0).setIcon(icTab[0]);
            tabLayout.getTabAt(1).setIcon(icTab[1]);
            tabLayout.getTabAt(2).setIcon(icTab[2]);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RegexDragonValidator(), "Regex");
        adapter.addFragment(new SpurFragment(), "Шпора");
        adapter.addFragment(new ThreoryFragment(), "Теория");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu != null) {
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
}
