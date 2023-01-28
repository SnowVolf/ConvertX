package ru.svolf.convertx.presentation.screens.settings

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

    var fontSize: Int
        get() = instance!!.prefs.getInt("Font.Size", 16)
        set(value) {
            instance!!.prefs.edit().putInt("Font.Size", value).apply()
        }
    val isTwiceBackAllowed: Boolean
        get() = instance!!.prefs.getBoolean("Interaction.Back", true)

}