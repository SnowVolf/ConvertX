package ru.SnowVolf.convertx.regexdragon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.other.Extras;
import ru.SnowVolf.convertx.utils.StringUtils;

/**
 * Created by Snow Volf on 08.03.2017, 7:06
 */

public class UrlExtentisions {
    public static void showChoiceDialog(final Activity activity, final String url) {
        try {
            final int OPEN_IN_BROWSER = 0;
            final int SHARE_IT = 1;
            final int COPY = 2;
            CharSequence[] titles = null;
            int[] ids = null;

            titles = new CharSequence[]{"Открыть в браузере", "Поделиться ссылкой", "Скопировать ссылку"};
            ids = new int[]{OPEN_IN_BROWSER, SHARE_IT, COPY};

            final int[] finalIds = ids;

            new MaterialDialog.Builder(activity)
                    .title(url)
                    .items(titles)
                    .itemsCallback((dialog, view, which, text) -> {
                        int id = finalIds[which];
                        try {
                            switch (id) {
                                case OPEN_IN_BROWSER:
                                    Extras.goLink(activity, url, "");
                                    break;
                                case SHARE_IT:
                                    Intent intent2 = shareText(url, url);
                                    activity.startActivity(intent2);
                                    break;
                                case COPY:
                                    StringUtils.copyToClipboard(activity, url);
                                    Toast.makeText(activity, R.string.copied2clipboard, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (Throwable ex) {
                        }
                        dialog.dismiss();
                    })
                    .show();

        } catch (Throwable ex) {
        }
    }

    public static Intent shareText(String subject, String text) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        if (!TextUtils.isEmpty(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        return intent;
    }
}
