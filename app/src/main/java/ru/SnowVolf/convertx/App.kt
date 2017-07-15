package ru.SnowVolf.convertx

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatDelegate
import org.acra.ACRA
import org.acra.ReportField.*
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes

/**
 * Created by Snow Volf on 06.02.2017.
 */
//acra
@ReportsCrashes(mailTo = "svolf15@yandex.ru", mode = ReportingInteractionMode.DIALOG, resDialogTheme = R.style.AppTheme_Dialog, resDialogIcon = R.drawable.ic_toast_warning, resDialogTitle = R.string.CrashTitle, resDialogText = R.string.CrashText, resDialogCommentPrompt = R.string.CrashWriteComment, resDialogOkToast = R.string.CrashThanks, customReportContent = arrayOf(APP_VERSION_NAME, APP_VERSION_CODE, ANDROID_VERSION, USER_COMMENT, PHONE_MODEL, CUSTOM_DATA, STACK_TRACE, LOGCAT)) //временный адрес, потом нужно будет зарегать свой хостинг
//resDialogEmailPrompt = R.string.MailSubject,
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        instance = this
    }

    var preferences: SharedPreferences? = null
    val prefs: SharedPreferences
        get() {
            if (preferences == null)
                preferences = PreferenceManager.getDefaultSharedPreferences(instance!!.applicationContext)
            return preferences!!
        }

    //Инициализация Acra
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        // The following line triggers the initialization of ACRA
        ACRA.init(this)
    }

    companion object {
        var instance: App? = null
            private set
    }

}
