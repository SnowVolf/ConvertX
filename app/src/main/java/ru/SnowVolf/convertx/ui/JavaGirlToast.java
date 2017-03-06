package ru.SnowVolf.convertx.ui;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import ru.SnowVolf.convertx.R;

/**
 * Created by Snow Volf on 27.12.2016
 */

public class JavaGirlToast {
    public static void showGirlToast(Activity ctx/*,String text*/){
        //TextView message;
        View toastGirl = ctx.getLayoutInflater().inflate(R.layout.wiget_toast_javagirl, null);
        //message = (TextView) ctx.findViewById(R.id.toast_message);
        //message.setText(text);
        Toast girl = new Toast(ctx.getApplicationContext());
        girl.setView(toastGirl);
        girl.setDuration(Toast.LENGTH_SHORT);
        girl.show();
    }
}