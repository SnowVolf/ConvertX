package ru.SnowVolf.convertx.other;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.text.util.LinkifyCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.fragments.UnicodeFragment;
import ru.SnowVolf.convertx.utils.SystemF;

public class AboutActivity extends AppCompatActivity {
    private TextView about, preambula;
    private Button changelogButton, gitButton;
    SharedPreferences preferences;
    String preambulaText = "Программа пригодится людям работающим с <b>*.apk</b> <br/>" +
            "<b>Функции:</b><br>"+
            "<b>*</b> Кодирование/Декодирование Unicode<br>"+
            "<b>*</b> Кодирование/Декодирование Base64<br>"+
            "<b>*</b> Кодирование/Декодирование HEX<br>"+
            "<b>*</b> Просмотр и копирование палитры цветов в HEX<br>" +
            "<b>*</b> Кодирование <b>Adler32/CRC32/MD5/SHA-1, 224, 384, 512</b><br>" +
            "<b>*</b> <b>Regex Dragon:</b> Проверка регулярных выражений<br>"+
            "<b>*</b> <b>XML</b> unescaper<br>"+
            "<b>*</b> <b>Timestamp</b> converter<br><br><br>";
    String aboutText = "Автор и разработчик: <b><a href=\"http://4pda.ru/forum/index.php?showuser=4324432\">Snow Volf</a></b> (Артем Жиганов)<br>\n" +
            "<b>Контакты: \n</b><a href=\"http://4pda.ru/forum/index.php?act=qms&mid=4324432\">QMS на 4pda</a> <b>|</b> <a href=\"mailto:svolf15@yandex.ru\">svolf15@yandex.ru</a><br>\n" +
            "<b>Особая благодарность</b>:\n <b><a href=\"http://4pda.ru/forum/index.php?showuser=2359960\">keks40</a></b>, <b><a href=\"http://4pda.ru/forum/index.php?showuser=1216287\">Enyby</a></b>, <b><a href=\"http://4pda.ru/forum/index.php?showuser=1457199\">mas-slon</a></b>\n" +
            "<br>"+
            "<b>Использованы разработки:</b><br>" +
            "<b>Android Open Source Project</b> © (2005–2016). Licensed under The Apache License, version 2.0<br>" +
            "<b>Android Support Library</b> © (2005–2016). Licensed under The Apache License, version 2.0<br>" +
            "<b>Gradle Retrolambda Plugin</b> © (2013) Evan Tatarka. Licensed under The Apache License, version 2.0<br>" +
            "<b>Material Dialogs</b> © (2014–2016) Adian Follestad. Licensed under The MIT License<br>" +
            "<b>Material Design Icons</b> © (2014–2016) Google/Templarian/<a href=\"https://materialdesignicons.com\">Material Design Icons Community</a>. Licensed under The Apache License, Version 2.0<br>" +
            "<b>ForPDA 4.0</b> © (2016-2017) ForPDA Team (Evgeniy Nizamiev, Alexandr Tainyk). Licensed under The Apache License, Version 2.0<br>"+
            //"<b>Apache Commons Lang</b> © (2001–2016) The Apache Software Fondunation. Licensed under The Apache License, version 2.0<br/>" +
            "<b>JavaGirl</b> © (2016) Snow Volf (Artem Zhiganov). Licensed under The MIT License<br>" +
            "<b>ACRA</b> © (2013–2016) Kevin Gaudin. Licensed under Apache License, Version 2.0<br>" +
            "<b>LeakCanary</b> © (2015) Square, Inc. Licensed under Apache License, Version 2.0<br><br>" +
            "Copyright © (2017) <b>Artem Zhiganov</b>";

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
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dr_about);
            getSupportActionBar().setSubtitle(Extras.getBuildName(getApplicationContext()));
        }
        about = (TextView) findViewById(R.id.about_content);
        preambula = (TextView) findViewById(R.id.about_preambula);
        changelogButton = (Button) findViewById(R.id.about_changelist);
        gitButton = (Button) findViewById(R.id.about_git);
        preambula.setText(Html.fromHtml(preambulaText));
        about.setText(Html.fromHtml(aboutText));
        /**
         * Чтобы ссылки, которые мы запихали в TextView, были кликабельны.
         * Метод с {@code Linkify.addLinks(about, Linkify.ALL);} у меня не заработал
         */
        about.setMovementMethod(LinkMovementMethod.getInstance());
        changelogButton.setOnClickListener(view -> showChangelog());
        gitButton.setOnClickListener(view -> ExploreSource());

        FloatingActionButton fab4pda = (FloatingActionButton) findViewById(R.id.fab_4pda);
        FloatingActionButton fabvk = (FloatingActionButton) findViewById(R.id.fab_vk);
        FloatingActionButton fabemail = (FloatingActionButton) findViewById(R.id.fab_email);

        fab4pda.setOnClickListener(view -> Extras.goLink(this, "https://4pda.ru/forum/index.php?showuser=4324432", "4pda"));
        fabvk.setOnClickListener(view -> Extras.goLink(this, "https://vk.com/snowvolf", "vkontakte"));
        fabemail.setOnClickListener(view -> Extras.goLink(this, "mailto:svolf15@yandex.ru", "mail"));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (menu != null){
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

    private void showChangelog() {
        Intent changelog = new Intent(this, ChangelistActivity.class);
        startActivity(changelog);
    }
    public void ExploreSource(){
        //не юзать getApplicationContext! AAAAAA!
        Extras.showGitDialog(this);
    }
}
