package ru.svolf.convertx.presentation.screens.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import ru.svolf.convertx.R
import ru.svolf.convertx.presentation.base.MainFragmentViewModel
import ru.svolf.convertx.utils.Interfacer

/**
 * Created by Snow Volf on 27.01.2017, 6:51
 */
class SettingsFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener {
    private lateinit var viewModel: MainFragmentViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        viewModel = ViewModelProvider(this)[MainFragmentViewModel::class.java]
        addPreferencesFromResource(R.xml.settings)
        //регистрируем слушателя
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
        //иначе будет падать на kit-kat
        setCurrentValue(preferenceScreen.findPreference("ITheme.Theme"))
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            "ITheme.Theme" -> {
                setCurrentValue(preferenceScreen.findPreference(key))
                activity?.let { LocalBroadcastManager.getInstance(it).sendBroadcast(Intent("org.openintents.action.REFRESH_THEME")) }
            }
            "AppFontSize" -> {
                viewModel.updateTextSize(sharedPreferences.getInt(key, 16))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }


    private fun setCurrentValue(listPreference: ListPreference?) {
        listPreference?.summary = listPreference?.entry
    }

    companion object {
        fun getThemeIndex(ctx: Context?): Int {
            return PreferenceManager.getDefaultSharedPreferences(ctx).getString("ITheme.Theme", Interfacer.Theme.LIGHT.ordinal.toString())!!.toInt()
        }
    }
}