package ru.SnowVolf.convertx.regexdragon

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.Toast

import com.afollestad.materialdialogs.MaterialDialog

import ru.SnowVolf.convertx.R
import ru.SnowVolf.convertx.settings.DefStrings
import ru.SnowVolf.convertx.ui.Toasty
import ru.SnowVolf.convertx.utils.StringUtils

/**
 * Created by Snow Volf on 08.03.2017, 7:06
 */

internal object UrlExtensions {
    fun showChoiceDialog(activity: Activity, url: String) {
        try {
            val OPEN_IN_BROWSER = 0
            val SHARE_IT = 1
            val COPY = 2
            val titles: Array<String>
            val ids: IntArray

            titles = arrayOf("Открыть в браузере", "Поделиться ссылкой", "Скопировать ссылку")
            ids = intArrayOf(OPEN_IN_BROWSER, SHARE_IT, COPY)

            val finalIds = ids

            MaterialDialog.Builder(activity)
                    .title(url)
                    .items(*titles)
                    .itemsCallback { dialog, view, which, text ->
                        val id = finalIds[which]
                        try {
                            when (id) {
                                OPEN_IN_BROWSER -> goLink(activity, url, "")
                                SHARE_IT -> {
                                    val intent2 = shareText(url, url)
                                    activity.startActivity(intent2)
                                }
                                COPY -> {
                                    StringUtils.copyToClipboard(activity, url)
                                    Toasty.info(activity, R.string.copied2clipboard.toString(), Toast.LENGTH_SHORT, true).show()
                                }
                            }
                        } catch (ignored: Throwable) {
                        }
                        dialog.dismiss()
                    }
                    .show()

        } catch (ignored: Throwable) {
        }

    }

    private fun shareText(subject: String, text: String): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        if (!TextUtils.isEmpty(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.type = "text/plain"
        return intent
    }

    /**
     * Чтобы не создавать один и тот же код много раз
     * 3 строки кода заменяем одной, потом юзаем где хотим
     */
    //контекст берем от Activity, иначе будет падать на некоторых прошивках
    fun goLink(context: Activity, link: String, info: String) {
        val uri = Uri.parse(link)
        val linkIntent = Intent(Intent.ACTION_VIEW, uri)
        //без этого флага крашится на некоторых устройствах
        linkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        Log.w(DefStrings.TAG, "Activity New Task ok")
        context.startActivity(Intent.createChooser(linkIntent, info))
    }
}
