package ru.svolf.convertx.presentation.screens.regex

import java.util.regex.Pattern

/**
 * Created by Snow Volf on 26.02.2017, 11:29
 */

internal class Flags {
    private val mData = ArrayList<Boolean>()
    val options = arrayOf("Case insensitive [i]", "Multiline [m]", "Comments [x]", "Dot all [s]", "Literal [-]", "Unicode Case [u]", "Unix Lines [d]")

    init {
        for (i in options.indices) {
            mData.add(i, false)
        }
    }

    fun add(id: Int) {
        mData[id] = true
    }

    fun remove(id: Int) {
        mData[id] = false
    }

    val selectedBooleans: BooleanArray
        get() {
            val data = BooleanArray(options.size)
            for (i in mData.indices) {
                data[i] = mData[i]
            }
            return data
        }
    /**
     * Чтобы не забыть
     * CANON_EQ (no effect on Android) == 128
     * CASE_INSENSITIVE == 2
     * COMMENTS == 4
     * DOTALL == 32
     * LITERAL == 16
     * MULTILINE == 8
     * UNICODE_CASE == 64
     * UNICODE_CHARACTER_CLASS (no effect on Android) == 256
     * UNIX_LINES == 1

     * все константы можно посмотреть на developer.android.com
     */

    val flags: Int
        get() {
            var data = 0
            if (mData[0]) {
                data = Pattern.CASE_INSENSITIVE
            }
            if (mData[1]) {
                return data or Pattern.MULTILINE
            }
            if (mData[2]) {
                return data or Pattern.COMMENTS
            }
            if (mData[3]) {
                return data or Pattern.DOTALL
            }
            if (mData[4]) {
                return data or Pattern.LITERAL
            }
            if (mData[5]) {
                return data or Pattern.UNICODE_CASE
            }
            if (mData[6]) {
                return data or Pattern.UNIX_LINES
            }
            return data
        }

    val flagString: String
        get() {
            var data = "/g"
            if (mData[0]) {
                data += "i"
            }
            if (mData[1]) {
                data += "m"
            }
            if (mData[2]) {
                data += "x"
            }
            if (mData[3]) {
                data += "s"
            }
            if (mData[4]) {
                data += "-"
            }
            if (mData[5]) {
                data += "u"
            }
            if (mData[6]) {
                data += "d"
            }
            return data
        }

}
