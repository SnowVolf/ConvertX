package ru.svolf.convertx

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by Snow Volf on 06.02.2017.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        PreferenceManager.setDefaultValues(this, R.xml.setting, false)
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
