package ru.SnowVolf.convertx.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatDelegate;

import ru.SnowVolf.convertx.R;

/**
 * Created by Snow Volf on 27.01.2017, 6:51
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        //иначе будет падать на kit-kat и ниже
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

    }

}
