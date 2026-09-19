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
            UnicodeRoute -> if (fromInput) {
                value.map { "\\u${it.code.toString(16).padStart(4, '0')}" }.joinToString("")
            } else Decoder.decodeUnicode(value)

            is Base64Route -> {
                val flags = base64Flags[mode.coerceIn(base64Flags.indices)]
                if (fromInput) Decoder.encodeBase64(value, flags) else Decoder.decodeBase64(
                    value,
                    flags
                )
            }

            is HexRoute -> if (mode == 1) {
                if (fromInput) Decoder.intToHex(value)
                else Decoder.hexToInt(value.removePrefix("0x").removePrefix("0X"))
            } else if (fromInput) {
                Decoder.toHexString(value)
            } else Decoder.decodeHexString(value.removePrefix("0x"))

            is TextToolRoute -> when (route.tool) {
                TextTool.ADLER32 -> Adler32().apply { update(value.toByteArray()) }.value.toString()
                TextTool.CRC32 -> CRC32().apply { update(value.toByteArray()) }.value.toString()
                TextTool.XML -> Decoder.unescapeXml(value)
                TextTool.TIMESTAMP -> Decoder.getNormalDate(value.toLong())
            }

            else -> value
        }

    private companion object {
        val base64Flags = intArrayOf(
            Base64.NO_WRAP, Base64.NO_CLOSE, Base64.NO_PADDING,
            Base64.URL_SAFE, Base64.CRLF, Base64.DEFAULT
        )
    }
}
