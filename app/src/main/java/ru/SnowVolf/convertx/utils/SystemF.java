package ru.SnowVolf.convertx.utils;

import android.app.Activity;
import android.content.Intent;

import ru.SnowVolf.convertx.R;

/**
 * Created by Snow Volf on 18.02.2017.
 */

public class SystemF {
    private static int sTheme;

    public final static int THEME_DEFAULT = 0;
    public final static int THEME_DARK = 1;
    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }
    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {
            default:
            case THEME_DEFAULT:
                activity.setTheme(R.style.AppTheme);
                break;
            case THEME_DARK:
                activity.setTheme(R.style.AppTheme_Dark);
                break;
        }
    }
}