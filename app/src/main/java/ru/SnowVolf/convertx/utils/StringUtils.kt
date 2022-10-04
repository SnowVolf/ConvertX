package ru.SnowVolf.convertx.utils

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import ru.SnowVolf.convertx.App


/**
 * Created by Snow Volf on 26.01.2017.
 */

object StringUtils {
    fun copyToClipboard(context: Context, code: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("char", code)
        clipboard.setPrimaryClip(clip)
    }

    fun readFromClipboard(): String? {
        val clipboard = App.Companion.instance?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboard.hasPrimaryClip()) {
            val description = clipboard.primaryClipDescription
            val data = clipboard.primaryClip
            if (data != null && description != null && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                return data.getItemAt(0).text.toString()
        }
        return null
    }
}