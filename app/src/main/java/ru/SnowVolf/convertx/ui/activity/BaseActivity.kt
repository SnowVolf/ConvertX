package ru.SnowVolf.convertx.ui.activity

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import ru.SnowVolf.convertx.settings.SettingsActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.SnowVolf.convertx.ui.Interfacer

open class BaseActivity : AppCompatActivity() {
    var preferences: SharedPreferences? = null

    //Theme
    private val mThemeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (SettingsActivity::class.java == this@BaseActivity.javaClass) {
                finish()
                startActivity(getIntent())
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } else recreate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        LocalBroadcastManager.getInstance(this).registerReceiver(mThemeReceiver, IntentFilter("org.openintents.action.REFRESH_THEME"))
        Interfacer.applyTheme(this)
        super.onCreate(savedInstanceState)
        INSTANCE = this
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mThemeReceiver)
        super.onDestroy()
    }

    companion object {
        private var INSTANCE: BaseActivity? = null
        val instance: BaseActivity?
            get() {
                if (INSTANCE == null) {
                    BaseActivity()
                }
                return INSTANCE
            }
    }
}