package ru.SnowVolf.convertx.settings;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.ui.Interfacer;

/**
 * Created by Snow Volf on 27.01.2017, 6:51
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String FRAGMENT_TAG = "main_settings_fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        //регистрируем слушателя
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        //иначе будет падать на kit-kat
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setCurrentValue((ListPreference) findPreference("ITheme.Theme"));
        Preference mFontSize = findPreference("Interaction.FontSize");
        mFontSize.setOnPreferenceClickListener(preference -> {
            showFontSizeDialog();
            return true;
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "ITheme.Theme":
                setCurrentValue((ListPreference) findPreference(key));
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("org.openintents.action.REFRESH_THEME"));
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            getActivity().setTitle(R.string.settings);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setCurrentValue(ListPreference listPreference){
        listPreference.setSummary(listPreference.getEntry());
    }

    @SuppressLint("SetTextI18n")
    protected void showFontSizeDialog() {
        @SuppressLint("InflateParams") View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_font_size, null);

        assert v != null;
        final SeekBar seekBar = (SeekBar) v.findViewById(R.id.value_seek_bar);
        seekBar.setProgress(Preferences.getFontSize());
        final EditText editText = (EditText) v.findViewById(R.id.value_text);
        final TextView sampleText = (TextView) v.findViewById(R.id.font_size_sample);
        sampleText.setTextSize(seekBar.getProgress() + 1);
        editText.setText(Integer.toString(seekBar.getProgress() + 1));

        v.findViewById(R.id.button_minus).setOnClickListener(v1 -> {
            if (seekBar.getProgress() > 0) {
                int i = seekBar.getProgress() - 1;
                seekBar.setProgress(i);
                sampleText.setTextSize(i + 1);
                editText.setText(Integer.toString(i + 1));
            }
        });
        v.findViewById(R.id.button_plus).setOnClickListener(v12 -> {
            if (seekBar.getProgress() < seekBar.getMax()) {
                int i = seekBar.getProgress() + 1;
                seekBar.setProgress(i);
                sampleText.setTextSize(i + 1);
                editText.setText(Integer.toString(i + 1));
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    seekBar.setProgress(Integer.valueOf(s.toString()) - 1);
                    editText.setSelection(s.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                sampleText.setTextSize(i + 1);
                editText.setText(Integer.toString(i + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dlg_font_size)
                .setView(v)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> Preferences.setFontSize(seekBar.getProgress()))
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> Preferences.getFontSize())
                .setNeutralButton(R.string.reset, null)
                .show();
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(v1 -> {
            seekBar.setProgress(15);
            sampleText.setTextSize(16);
            Preferences.setFontSize(15);
        });
    }

    public static int getThemeIndex(Context ctx) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(ctx).getString("ITheme.Theme", String.valueOf(Interfacer.Theme.LIGHT.ordinal())));
    }
}