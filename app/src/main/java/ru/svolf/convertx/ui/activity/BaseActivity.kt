package ru.svolf.convertx.ui.activity

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.svolf.convertx.ui.Interfacer

open class BaseActivity : AppCompatActivity() {
    var preferences: SharedPreferences? = null

    //Theme
    private val mThemeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            finish()
            startActivity(getIntent())
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        LocalBroadcastManager.getInstance(this).registerReceiver(mThemeReceiver, IntentFilter("org.openintents.action.REFRESH_THEME"))
        Interfacer.applyTheme(this)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mThemeReceiver)
        super.onDestroy()
    }

}