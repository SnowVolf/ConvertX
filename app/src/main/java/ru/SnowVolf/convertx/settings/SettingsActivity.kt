package ru.SnowVolf.convertx.settings

import ru.SnowVolf.convertx.settings.DefStrings.TAG
import ru.SnowVolf.convertx.settings.DefStrings.GIRL
import ru.SnowVolf.convertx.ui.activity.BaseActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import ru.SnowVolf.convertx.settings.DefStrings
import ru.SnowVolf.convertx.R
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import ru.SnowVolf.convertx.settings.SettingsFragment

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "SettingsActivity")
        Log.w(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //иначе будет падать на kit-kat и ниже
        Log.i(GIRL, "VectorCompat : Disabled")
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        Log.i(GIRL, "VectorCompat : Enabled")
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_action_arrow_back)
        if (savedInstanceState != null) return
        // Create the fragment only when the activity is created for the first time.
        // ie. not after orientation changes
        var fragment = fragmentManager.findFragmentByTag(SettingsFragment.FRAGMENT_TAG)
        if (fragment == null) {
            fragment = SettingsFragment()
        }
        fragmentManager.beginTransaction()
            .replace(R.id.settings_frame_container, fragment, SettingsFragment.FRAGMENT_TAG)
            .commit()
    }

    //лень было делать еще один menu-res
    //поэтому решил нарисовать menu программно. никто же не против?
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}