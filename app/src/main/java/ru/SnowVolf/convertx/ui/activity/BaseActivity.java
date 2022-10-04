package ru.SnowVolf.convertx.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import ru.SnowVolf.convertx.settings.SettingsActivity;
import ru.SnowVolf.convertx.ui.Interfacer;

public class BaseActivity extends AppCompatActivity {
    private static BaseActivity INSTANCE = null;
    SharedPreferences preferences;
    //Theme
    private final BroadcastReceiver mThemeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SettingsActivity.class.equals(BaseActivity.this.getClass())){
                finish();
                startActivity(getIntent());
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else recreate();
        }
    };
    public BaseActivity(){

    }

    public static BaseActivity getInstance() {
        if (INSTANCE == null) {
            new BaseActivity();
        }
        return INSTANCE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mThemeReceiver, new IntentFilter("org.openintents.action.REFRESH_THEME"));
        Interfacer.applyTheme(this);
        super.onCreate(savedInstanceState);
        INSTANCE = this;
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mThemeReceiver);
        super.onDestroy();
    }

}

