package ru.SnowVolf.convertx.Other;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import ru.SnowVolf.convertx.R;

/**
 * Created by Snow Volf on 27.01.2017.
 */

public class Extras {
    public static void showAboutDialog(Context activity){
        new MaterialDialog.Builder(activity)
                .title("О программе")
                .content("Автор и разработчик:\nSnow Volf (Артём Жиганов)\n\n" +
                        "Особая благодарность:\nkeks40, Enyby, mas-slon\nPeople from 4pda.ru")
                .positiveText("ok")
                .show();
    }
    public static void showSnackBar(View v){
        Snackbar.make(v, R.string.copied2clipboard, Snackbar.LENGTH_SHORT).show();
    }

}
