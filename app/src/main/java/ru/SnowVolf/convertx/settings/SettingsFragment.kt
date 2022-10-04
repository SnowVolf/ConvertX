package ru.SnowVolf.convertx.settings

import android.preference.PreferenceFragment
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import ru.SnowVolf.convertx.R
import androidx.appcompat.app.AppCompatDelegate
import android.preference.ListPreference
import android.preference.Preference
import android.preference.Preference.OnPreferenceClickListener
import android.content.SharedPreferences
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.content.Intent
import android.annotation.SuppressLint
import android.content.Context
import android.widget.SeekBar
import android.widget.EditText
import android.widget.TextView
import android.text.TextWatcher
import android.text.Editable
import android.widget.SeekBar.OnSeekBarChangeListener
import android.content.DialogInterface
import android.preference.PreferenceManager
import android.view.View
import androidx.appcompat.app.AlertDialog
import ru.SnowVolf.convertx.ui.Interfacer
import java.lang.Exception

/**
 * Created by Snow Volf on 27.01.2017, 6:51
 */
class SettingsFragment : PreferenceFragment(), OnSharedPreferenceChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.setting)
        //регистрируем слушателя
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        //иначе будет падать на kit-kat
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setCurrentValue(findPreference("ITheme.Theme") as ListPreference)
        val mFontSize = findPreference("Interaction.FontSize")
        mFontSize.onPreferenceClickListener = OnPreferenceClickListener { preference: Preference? ->
            showFontSizeDialog()
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            "ITheme.Theme" -> {
                setCurrentValue(findPreference(key) as ListPreference)
                LocalBroadcastManager.getInstance(activity).sendBroadcast(Intent("org.openintents.action.REFRESH_THEME"))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            activity.setTitle(R.string.settings)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setCurrentValue(listPreference: ListPreference) {
        listPreference.summary = listPreference.entry
    }

    @SuppressLint("SetTextI18n")
    protected fun showFontSizeDialog() {
        @SuppressLint("InflateParams") val v = activity.layoutInflater.inflate(R.layout.dialog_font_size, null)!!
        val seekBar = v.findViewById<View>(R.id.value_seek_bar) as SeekBar
        seekBar.progress = Preferences.fontSize
        val editText = v.findViewById<View>(R.id.value_text) as EditText
        val sampleText = v.findViewById<View>(R.id.font_size_sample) as TextView
        sampleText.textSize = (seekBar.progress + 1).toFloat()
        editText.setText(Integer.toString(seekBar.progress + 1))
        v.findViewById<View>(R.id.button_minus).setOnClickListener { v1: View? ->
            if (seekBar.progress > 0) {
                val i = seekBar.progress - 1
                seekBar.progress = i
                sampleText.textSize = (i + 1).toFloat()
                editText.setText(Integer.toString(i + 1))
            }
        }
        v.findViewById<View>(R.id.button_plus).setOnClickListener { v12: View? ->
            if (seekBar.progress < seekBar.max) {
                val i = seekBar.progress + 1
                seekBar.progress = i
                sampleText.textSize = (i + 1).toFloat()
                editText.setText(Integer.toString(i + 1))
            }
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != "") {
                    seekBar.progress = Integer.valueOf(s.toString()) - 1
                    editText.setSelection(s.length)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                sampleText.textSize = (i + 1).toFloat()
                editText.setText(Integer.toString(i + 1))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        val dialog = AlertDialog.Builder(activity)
            .setTitle(R.string.dlg_font_size)
            .setView(v)
            .setPositiveButton(R.string.ok) { dialogInterface: DialogInterface?, i: Int -> Preferences.fontSize = seekBar.progress }
            .setNegativeButton(android.R.string.cancel) { dialogInterface: DialogInterface?, i: Int -> Preferences.fontSize }
            .setNeutralButton(R.string.reset, null)
            .show()
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener { v1: View? ->
            seekBar.progress = 15
            sampleText.textSize = 16f
            Preferences.fontSize = 15
        }
    }

    companion object {
        const val FRAGMENT_TAG = "main_settings_fragment"
        fun getThemeIndex(ctx: Context?): Int {
            return PreferenceManager.getDefaultSharedPreferences(ctx).getString("ITheme.Theme", Interfacer.Theme.LIGHT.ordinal.toString())!!.toInt()
        }
    }
}