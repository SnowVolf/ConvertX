package ru.svolf.convertx.settings

import ru.svolf.convertx.App.Companion.instance

/**
 * Created by Snow Volf on 30.06.2017, 7:32
 */
object Preferences {
    fun setSpinnerPosition(str: String?, i: Int) {
        instance!!.prefs.edit().putInt(str, i).apply()
    }

    fun getSpinnerPosition(str: String?): Int {
        return instance!!.prefs.getInt(str, 0)
    }

    var drawerPosition: Int
        get() = instance!!.prefs.getInt("drawer.position", 1)
        set(i) {
            instance!!.prefs.edit().putInt("drawer.position", i).apply()
        }
    var fontSize: Int
        get() = instance!!.prefs.getInt("Font.Size", 16)
        set(value) {
            instance!!.prefs.edit().putInt("Font.Size", value).apply()
        }
    val isTwiceBackAllowed: Boolean
        get() = instance!!.prefs.getBoolean("Interaction.Back", true)
    var colorBackground: Int
        get() = instance!!.prefs.getInt("color.background", 0xeeeeee)
        set(colorBackground) {
            instance!!.prefs.edit().putInt("color.background", colorBackground).apply()
        }
    var colorText: Int
        get() = instance!!.prefs.getInt("color.text", 0x000000)
        set(colorText) {
            instance!!.prefs.edit().putInt("color.text", colorText).apply()
        }
    var colorSize: Int
        get() = instance!!.prefs.getInt("color.textSize", 16)
        set(colorSize) {
            instance!!.prefs.edit().putInt("color.textSize", colorSize).apply()
        }
    var textString: String?
        get() = instance!!.prefs.getString("color.textString", "Snow Volf")
        set(textString) {
            instance!!.prefs.edit().putString("color.textString", textString).apply()
        }
}