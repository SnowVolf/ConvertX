package ru.svolf.convertx

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.android.material.color.DynamicColors

/**
 * Created by Snow Volf on 06.02.2017.
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        PreferenceManager.setDefaultValues(this, R.xml.settings, false)
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

    var preferences: SharedPreferences? = null
    val prefs: SharedPreferences
        get() {
            if (preferences == null)
                preferences = PreferenceManager.getDefaultSharedPreferences(instance!!.applicationContext)
            return preferences!!
        }

    companion object {
        var instance: App? = null
            private set
    }

}
