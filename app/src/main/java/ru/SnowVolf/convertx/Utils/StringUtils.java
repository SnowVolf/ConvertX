package ru.SnowVolf.convertx.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;


/**
 * Created by Snow Volf on 26.01.2017.
 */

public class StringUtils {
    public static void copyToClipboard(Context context, String code) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("char", code);
        clipboard.setPrimaryClip(clip);

    }
}