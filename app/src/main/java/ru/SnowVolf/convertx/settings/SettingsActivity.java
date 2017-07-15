package ru.SnowVolf.convertx.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.ui.activity.BaseActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(DefStrings.INSTANCE.getTAG(), "SettingsActivity");
        Log.w(DefStrings.INSTANCE.getTAG(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //иначе будет падать на kit-kat и ниже
        Log.i(DefStrings.INSTANCE.getGIRL(), "VectorCompat : Disabled");
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Log.i(DefStrings.INSTANCE.getGIRL(), "VectorCompat : Enabled");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        if (savedInstanceState != null) return;
        // Create the fragment only when the activity is created for the first time.
        // ie. not after orientation changes
        Fragment fragment = getFragmentManager().findFragmentByTag(SettingsFragment.FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new SettingsFragment();
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.settings_frame_container, fragment, SettingsFragment.FRAGMENT_TAG)
                .commit();
    }

    //лень было делать еще один menu-res
    //поэтому решил нарисовать menu программно. никто же не против?
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
            onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
