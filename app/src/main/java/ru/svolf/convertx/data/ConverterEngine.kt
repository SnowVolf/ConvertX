package ru.svolf.convertx.data

import android.util.Base64
import ru.svolf.convertx.presentation.navigation.AppRoute
import ru.svolf.convertx.presentation.navigation.Base64Route
import ru.svolf.convertx.presentation.navigation.HexRoute
import ru.svolf.convertx.presentation.navigation.TextTool
import ru.svolf.convertx.presentation.navigation.TextToolRoute
import ru.svolf.convertx.presentation.navigation.UnicodeRoute
import ru.svolf.convertx.utils.DecoderConst
import ru.svolf.convertx.utils.algorhitms.Decoder
import java.util.zip.Adler32
import java.util.zip.CRC32
import javax.inject.Inject

class ConverterEngine @Inject constructor() {
    fun decoderFor(route: AppRoute): Int = when (route) {
        UnicodeRoute -> DecoderConst.UNICODE
        is Base64Route -> DecoderConst.BASE64
        is HexRoute -> DecoderConst.HEX
        else -> -1
    }

    suspend fun convert(route: AppRoute, value: String, fromInput: Boolean, mode: Int): String =
        when (route) {
            UnicodeRoute -> convertUnicode(value, fromInput)
            is Base64Route -> convertBase64(value, fromInput, mode)
            is HexRoute -> convertHex(value, fromInput, mode)
            is TextToolRoute -> convertTextTool(value, route.tool)
            else -> value
        }

    private suspend fun convertUnicode(value: String, fromInput: Boolean): String =
        if (fromInput) {
            value.map { "\\u${it.code.toString(HEX_RADIX).padStart(UNICODE_HEX_DIGITS, '0')}" }
                .joinToString("")
        } else {
            Decoder.decodeUnicode(value)
        }

    private suspend fun convertBase64(value: String, fromInput: Boolean, mode: Int): String {
        val flags = base64Flags[mode.coerceIn(base64Flags.indices)]
        return if (fromInput) Decoder.encodeBase64(value, flags) else Decoder.decodeBase64(value, flags)
    }

    private suspend fun convertHex(value: String, fromInput: Boolean, mode: Int): String =
        if (mode == HEX_NUMBER_MODE) {
            if (fromInput) Decoder.intToHex(value)
            else Decoder.hexToInt(value.removePrefix("0x").removePrefix("0X"))
        } else if (fromInput) {
            Decoder.toHexString(value)
        } else {
            Decoder.decodeHexString(value.removePrefix("0x"))
        }

    private suspend fun convertTextTool(value: String, tool: TextTool): String = when (tool) {
        TextTool.ADLER32 -> Adler32().apply { update(value.toByteArray()) }.value.toString()
        TextTool.CRC32 -> CRC32().apply { update(value.toByteArray()) }.value.toString()
        TextTool.XML -> Decoder.unescapeXml(value)
        TextTool.TIMESTAMP -> Decoder.getNormalDate(value.toLong())
    }

    private companion object {
        const val HEX_RADIX = 16
        const val HEX_NUMBER_MODE = 1
        const val UNICODE_HEX_DIGITS = 4

        val base64Flags = intArrayOf(
            Base64.NO_WRAP, Base64.NO_CLOSE, Base64.NO_PADDING,
            Base64.URL_SAFE, Base64.CRLF, Base64.DEFAULT
        )
    }
}
