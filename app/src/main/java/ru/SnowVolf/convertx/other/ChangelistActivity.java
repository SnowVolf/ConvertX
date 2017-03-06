package ru.SnowVolf.convertx.other;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.utils.SystemF;

public class ChangelistActivity extends AppCompatActivity {
    private TextView changelog;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        if (preferences.getBoolean("Theme.Theme", true)) {
            preferences.edit().putBoolean("Theme.Theme", true).apply();
            SystemF.onActivityCreateSetTheme(this);
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.AppTheme_Dark);
            preferences.edit().putBoolean("Theme.Theme", false).apply();
        }
        //иначе будет падать на KK и ниже
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_changelist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_changelog);
            getSupportActionBar().setSubtitle(Extras.getBuildName(this));
        }
        changelog = (TextView) findViewById(R.id.text_changelog);

        final StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("CHANGELOG.javagirl"), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("<br/>");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Typeface Mono = Typeface.createFromAsset(getAssets(), "fonts/RobotoMono-Regular.ttf");
        changelog.setTypeface(Mono);
        changelog.setText(Html.fromHtml(sb.toString()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu != null) {
            menu.clear();
        } else
            menu = new MenuBuilder(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;

    }
}
