package ru.svolf.convertx.utils

import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import ru.svolf.convertx.App.Companion.instance

/**
 * Created by Snow Volf on 29.06.2017, 20:48
 */
object Interfacer {
    fun applyTheme(theme: Int) {
        when (Theme.values()[theme]) {
            Theme.LIGHT -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            Theme.DARK -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        }

    }

    fun getThemedResourceId(ctx: Context, attrId: Int): Int {
        return getThemedResourceId(ctx.theme, attrId)
    }

    fun getThemedResourceId(theme: Resources.Theme, attrId: Int): Int {
        val a = theme.obtainStyledAttributes(intArrayOf(attrId))
        val result = a.getResourceId(0, -1)
        a.recycle()
        return result
    }

    fun getThemedColor(ctx: Context, attrId: Int): Int {
        return getThemedColor(ctx.theme, attrId)
    }

    fun getThemedColor(theme: Resources.Theme, attrId: Int): Int {
        val a = theme.obtainStyledAttributes(intArrayOf(attrId))
        val result = a.getResourceId(0, -1)
        a.recycle()
        return result
    }

    fun setThemeIndex(index: Int) {
        instance!!.prefs.edit().putInt("ITheme.Theme", index).apply()
    }

    enum class Theme {
        LIGHT, DARK
    }
}