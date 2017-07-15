package ru.SnowVolf.convertx.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import ru.SnowVolf.convertx.App;
import ru.SnowVolf.convertx.R;
import ru.SnowVolf.convertx.settings.SettingsFragment;

/**
 * Created by Snow Volf on 29.06.2017, 20:48
 */

public abstract class Interfacer {
    public enum Theme{
        LIGHT,
        DARK,
        PURE_DARK
    }

    public static void applyTheme(Activity ctx){
        int theme;
        switch (Theme.values()[SettingsFragment.getThemeIndex(ctx)]){
            case LIGHT:
                theme = R.style.MaterialDrawerTheme_Light_DarkToolbar;
                break;
            case DARK:
                theme = R.style.MaterialDrawerTheme_TranslucentStatus;
                break;
            case PURE_DARK:
                theme = R.style.MaterialDrawerTheme_TranslucentStatusBlack;
                break;
            default:
                theme = R.style.MaterialDrawerTheme_Light_DarkToolbar;
                break;
        }
        ctx.setTheme(theme);
    }

    public static int getThemedResourceId(Context ctx, int attrId){
        return getThemedResourceId(ctx.getTheme(), attrId);
    }

    public static int getThemedResourceId(Resources.Theme theme, int attrId){
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        int result = a.getResourceId(0, -1);
        a.recycle();
        return result;
    }

    public static int getThemedColor(Context ctx, int attrId){
        return getThemedColor(ctx.getTheme(), attrId);
    }

    public static int getThemedColor(Resources.Theme theme, int attrId){
        TypedArray a = theme.obtainStyledAttributes(new int[]{attrId});
        int result = a.getResourceId(0, -1);
        a.recycle();
        return result;
    }

    public static void setThemeIndex(int index){
        //noinspection ConstantConditions
        App.Companion.getInstance().getPrefs().edit().putInt("ITheme.Theme", index).apply();
    }

}
