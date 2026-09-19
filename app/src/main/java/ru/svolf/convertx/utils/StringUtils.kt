package ru.svolf.convertx.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context


/**
 * Created by Snow Volf on 26.01.2017.
 */

object StringUtils {
    fun copyToClipboard(context: Context, code: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("char", code)
        clipboard.setPrimaryClip(clip)
    }

}
