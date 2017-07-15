package ru.SnowVolf.convertx.settings;

import ru.SnowVolf.convertx.App;

/**
 * Created by Snow Volf on 30.06.2017, 7:32
 */

public class Preferences {
    public static void setSpinnerPosition(String str, int i) {
        App.Companion.getInstance().getPrefs().edit().putInt(str, i).apply();
    }

    public static int getSpinnerPosition(String str){
        return App.Companion.getInstance().getPrefs().getInt(str, 0);
    }

    public static void setDrawerPosition(int i) {
        App.Companion.getInstance().getPrefs().edit().putInt("drawer.position", i).apply();
    }

    public static int getDrawerPosition(){
        return App.Companion.getInstance().getPrefs().getInt("drawer.position", 1);
    }

    public static int getFontSize() {
        return App.Companion.getInstance().getPrefs().getInt("Font.Size", 16);
    }

    public static void setFontSize(int value) {
        App.Companion.getInstance().getPrefs().edit().putInt("Font.Size", value).apply();
    }

    public static boolean isTwiceBackAllowed(){
        return App.Companion.getInstance().getPrefs().getBoolean("Interaction.Back", true);
    }

    public static int getColorBackground(){
        return App.Companion.getInstance().getPrefs().getInt("color.background", 0xeeeeee);
    }

    public static void setColorBackground(int colorBackground){
        App.Companion.getInstance().getPrefs().edit().putInt("color.background", colorBackground).apply();
    }

    public static int getColorText(){
        return App.Companion.getInstance().getPrefs().getInt("color.text", 0x000000);
    }

    public static void setColorText(int colorText){
        App.Companion.getInstance().getPrefs().edit().putInt("color.text", colorText).apply();
    }

    public static int getColorSize(){
        return App.Companion.getInstance().getPrefs().getInt("color.textSize", 16);
    }

    public static void setColorSize(int colorSize){
        App.Companion.getInstance().getPrefs().edit().putInt("color.textSize", colorSize).apply();
    }

    public static String getTextString(){
        return App.Companion.getInstance().getPrefs().getString("color.textString", "Snow Volf");
    }

    public static void setTextString(String textString){
        App.Companion.getInstance().getPrefs().edit().putString("color.textString", textString).apply();
    }
}
