package ru.SnowVolf.convertx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static ru.SnowVolf.convertx.Other.Extras.showAboutDialog;
import static ru.SnowVolf.convertx.Other.Extras.showSnackBar;
import static ru.SnowVolf.convertx.Utils.StringUtils.copyToClipboard;


public class MainActivity extends AppCompatActivity {
    public EditText ed_unicode;
    public EditText ed_utf;
    SharedPreferences sp;
    public static final String APP_PREFS = "JavaGirlPrefs-ConvertX";
    public static final String UNICODE = "Unicode.Value";
    public static final String UTF = "Utf.Value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //иначе будет падать на KK и ниже
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);
        ed_unicode = (EditText) findViewById(R.id.unicodeText);
        ed_utf = (EditText) findViewById(R.id.utfText);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        if (sp.contains(UTF) && sp.contains(UNICODE)){
            ed_utf.setText(sp.getString(UTF, ""));
            ed_unicode.setText(sp.getString(UNICODE, ""));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu != null) {
            menu.clear();
        } else
            menu = new MenuBuilder(this);
        //добавляем пункты меню
        menu.add(R.string.convert)
                .setIcon(R.drawable.ic_menu_convert)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setOnMenuItemClickListener(menuItem -> {
                    StartConverting();
                    return true;
                });
        menu.add(R.string.clear_all)
                .setIcon(R.drawable.ic_menu_clear_all)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setOnMenuItemClickListener(menuItem -> {
                    clearAllText();
                    return true;
                });

        menu.add(R.string.settings)
                .setIcon(R.drawable.ic_menu_settings)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                .setOnMenuItemClickListener(menuItem -> {
                    Toast.makeText(getBaseContext(), "В одном из следующих обновлений...", Toast.LENGTH_LONG).show();
                    return true;
                });
        SubMenu subMenu = menu.addSubMenu(R.string.other);
        subMenu.add(R.string.about_program).setOnMenuItemClickListener(menuItem -> {showAboutDialog(this); return true;});
        subMenu.add(R.string.changelog).setOnMenuItemClickListener(menuItem -> {showChangelog(); return true;});
        subMenu.add(R.string.github).setOnMenuItemClickListener(menuItem -> {VisitGit(); return true;});
        return true;
    }

    public void StartConverting() {
        SharedPreferences.Editor editor = sp.edit();
        if (!ed_unicode.getText().toString().isEmpty() && ed_utf.getText().toString().isEmpty()) {
            String unicodeContent = ed_unicode.getText().toString();
            String text = StringEscapeUtils.unescapeJava(unicodeContent);
            ed_utf.setText(text);
            editor.putString(UTF, text);
            editor.apply();
            Toast.makeText(this, R.string.done, Toast.LENGTH_SHORT).show();
        } else if (ed_unicode.getText().toString().isEmpty() && !ed_utf.getText().toString().isEmpty()) {
            String utfSource = ed_utf.getText().toString();
            StringBuilder unicodeValue = new StringBuilder();
            for (int i = 0; i < utfSource.length(); i++)
                unicodeValue.append("\\u").append(Integer.toHexString(utfSource.charAt(i) | 0x10000).substring(1));
            ed_unicode.setText(unicodeValue);
            editor.putString(UNICODE, unicodeValue.toString());
            editor.apply();
            sp.edit().putString("Unicode.Value", unicodeValue.toString()).apply();

        }
    }
    public void VisitGit(){
        Uri uri = Uri.parse("https://github.com/SnowVolf/ConvertX/");
        Intent gitIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(Intent.createChooser(gitIntent, "Изучить исходники"));
    }
    //собираем инфу о приложении
    private void showChangelog() {
        final StringBuilder sb = new StringBuilder();
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("changelog"), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        new MaterialDialog.Builder(this)
                .title(getBuildName(this))
                .content(sb)
                .positiveText("ok")
                .show();
    }
    public static String getBuildName(Context context){
        String programBuild = context.getString(R.string.app_name);
        try{
            String pkg = context.getPackageName();
            PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(pkg, PackageManager.GET_META_DATA);
            programBuild += " v."+pkgInfo.versionName+" build "+pkgInfo.versionCode;


        }catch (PackageManager.NameNotFoundException e1){
            e1.printStackTrace();
        }return programBuild;
    }


    public void copyUnicode(View v){
        copyToClipboard(this, ed_unicode.getText().toString());
        showSnackBar(v);
    }
    public void copyUtf(View v){
        copyToClipboard(this, ed_utf.getText().toString());
        showSnackBar(v);
    }

    public void clearUnicode(View v){
        ed_unicode.setText("");
        sp.edit().putString(UNICODE, "").apply();
    }
    public void clearUtf(View v){
        ed_utf.setText("");
        sp.edit().putString(UTF, "").apply();
    }
    public void clearAllText(){
        ed_utf.setText("");
        sp.edit().putString(UTF, "").apply();
        ed_unicode.setText("");
        sp.edit().putString(UNICODE, "").apply();
        Toast.makeText(this, R.string.cleared, Toast.LENGTH_SHORT).show();
    }
}
