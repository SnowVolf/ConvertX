package ru.svolf.convertx.ui

import ru.svolf.convertx.App.Companion.instance
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import ru.svolf.convertx.settings.SettingsFragment
import ru.svolf.convertx.R

/**
 * Created by Snow Volf on 29.06.2017, 20:48
 */
object Interfacer {
    fun applyTheme(ctx: Activity) {
        val theme: Int
        theme = when (Theme.values()[SettingsFragment.getThemeIndex(ctx)]) {
            Theme.LIGHT -> R.style.MaterialDrawerTheme_Light_DarkToolbar
            Theme.DARK -> R.style.MaterialDrawerTheme_TranslucentStatusBlack
            else -> R.style.MaterialDrawerTheme_Light_DarkToolbar
        }
        ctx.setTheme(theme)
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