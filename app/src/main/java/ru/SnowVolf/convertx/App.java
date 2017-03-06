package ru.SnowVolf.convertx;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.content.res.AppCompatResources;

import com.squareup.leakcanary.ActivityRefWatcher;
import com.squareup.leakcanary.LeakCanary;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.lang.reflect.Field;
import java.util.HashMap;

import static org.acra.ReportField.ANDROID_VERSION;
import static org.acra.ReportField.APP_VERSION_CODE;
import static org.acra.ReportField.APP_VERSION_NAME;
import static org.acra.ReportField.CUSTOM_DATA;
import static org.acra.ReportField.LOGCAT;
import static org.acra.ReportField.PHONE_MODEL;
import static org.acra.ReportField.STACK_TRACE;
import static org.acra.ReportField.USER_COMMENT;

/**
 * Created by Snow Volf on 06.02.2017.
 */
//acra
@ReportsCrashes(
        mailTo = "svolf15@yandex.ru",//временный адрес, потом нужно будет зарегать свой хостинг
        //resDialogEmailPrompt = R.string.MailSubject,
        mode = ReportingInteractionMode.DIALOG,
        resDialogTheme = R.style.AppTheme_Dialog,
        resDialogIcon = R.drawable.ic_warning,
        resDialogTitle = R.string.CrashTitle,
        resDialogText = R.string.CrashText,
        resDialogCommentPrompt = R.string.CrashWriteComment,
        resDialogOkToast = R.string.CrashThanks,
        customReportContent = {APP_VERSION_NAME, APP_VERSION_CODE, ANDROID_VERSION, USER_COMMENT, PHONE_MODEL, CUSTOM_DATA, STACK_TRACE, LOGCAT}
)
public class App extends Application {
    public static HashMap<Integer, Drawable> drawableHashMap = new HashMap<>();
    private static App INSTANCE;

    public static App getInstance() {
        return INSTANCE;
    }

    public static Drawable getAppDrawable(int id) {
        return drawableHashMap.get(id);
    }

    public static Context ctx() {
        return getInstance();
    }

    @Override
    public final void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        INSTANCE = this;

        //Для более быстрого доступа к drawable при работе программы
        for (Field f : R.drawable.class.getFields()) {
            try {
                if (!f.getName().contains("abc_"))
                    drawableHashMap.put(f.getInt(f), AppCompatResources.getDrawable(App.ctx(), f.getInt(f)));
            } catch (Exception ignore) {
            }
        }
    }

    private SharedPreferences preferences;
    public SharedPreferences getPreferences(){
        if(preferences == null)
            preferences = PreferenceManager.getDefaultSharedPreferences(INSTANCE.getApplicationContext());
        return preferences;
    }

    //Инициализация Acra
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
    }

}
