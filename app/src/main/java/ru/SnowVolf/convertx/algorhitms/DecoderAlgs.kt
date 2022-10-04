package ru.SnowVolf.convertx.algorhitms

import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Snow Volf on 21.02.2017, 8:42
 */
object DecoderAlgs {
    /**
     * Расшифровка Unicode
     */
    fun unescapeJava(tracer: String): String {
        var aChar: Char
        val len = tracer.length
        val outBuffer = StringBuilder(len)
        var x = 0
        while (x < len) {
            aChar = tracer[x++]
            if (aChar == '\\') {
                aChar = tracer[x++]
                if (aChar == 'u') {
                    // Читаем массив
                    var value = 0
                    for (i in 0..3) {
                        aChar = tracer[x++]
                        value = when (aChar) {
                            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> (value shl 4) + aChar.code - '0'.code
                            'a', 'b', 'c', 'd', 'e', 'f' -> (value shl 4) + 10 + aChar.code - 'a'.code
                            'A', 'B', 'C', 'D', 'E', 'F' -> (value shl 4) + 10 + aChar.code - 'A'.code
                            else -> throw IllegalArgumentException(
                                "Malformed   \\uxxxx   encoding."
                            )
                        }
                    }
                    outBuffer.append(value.toChar())
                } else {
                    if (aChar == 't') aChar = '\t' else if (aChar == 'r') aChar = '\r' else if (aChar == 'n') aChar =
                        '\n' else if (aChar == 'f') aChar = '\u000c'
                    outBuffer.append(aChar)
                }
            } else outBuffer.append(aChar)
        }
        return outBuffer.toString()
    }

    /**
     * Расшифровка XML
     */
    fun unescapeXml(xml: String): String {
        //Отыскиваем escape-символы с помощью регулярки
        val xmlEntityRegex = Pattern.compile("&(#?)([^;]+);")
        //Matcher требует StringBuffer вместо StringBuilder
        val unescapedOutput = StringBuffer(xml.length)
        val m = xmlEntityRegex.matcher(xml)
        var builtinEntities: Map<String?, String?>? = null
        //Переменные
        var entity: String?
        var hashmark: String?
        var ent: String
        var code: Int
        //Пока найдено
        while (m.find()) {
            ent = m.group(2)
            hashmark = m.group(1)
            if (hashmark != null && hashmark.length > 0) {
                code = ent.toInt()
                entity = Character.toString(code.toChar())
            } else {
                //must be a non-numerical entity
                if (builtinEntities == null) {
                    builtinEntities = buildBuiltinXMLEntityMap()
                }
                entity = builtinEntities[ent]
                if (entity == null) {
                    //Не знаем что за символ -- бросаем его нахрен
                    entity = "&$ent;"
                }
            }
            m.appendReplacement(unescapedOutput, entity)
        }
        m.appendTail(unescapedOutput)
        return unescapedOutput.toString()
    }

    private fun buildBuiltinXMLEntityMap(): Map<String?, String?> {
        //По HashMap сопоставяем escape и заменяем на нормальные символы
        val entities: MutableMap<String?, String?> = HashMap(10)
        entities["lt"] = "<"
        entities["gt"] = ">"
        entities["amp"] = "&"
        entities["apos"] = "'"
        entities["quot"] = "\""
        return entities
    }

    /**
     * Применяем буржуйскую локаль, чтоб с локальной датой не ебаться
     * Из-за нее бывают ошибки на некоторых телефонах
     * Например на моём Wileyfox Swift с кастомом RR N v.5.8.2 (Lineage OS 14.1) всё нормально,
     * но на HTC One Mini 2 с кастомом CyanogenMod 13,
     * при попытке получить дату из Timestamp идёт вылет. Такая вот она, локальная дата
     */
    fun getNormalDate(time: Long): String {
        val date = Date(time)
        val format: Format = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH)
        return format.format(date)
    }

    fun hexDecode(text: String): ByteArray {
        val l = text.length
        val data = ByteArray(l / 2)
        var i = 0
        while (i < l) {
            data[i / 2] = ((text[i].digitToIntOrNull(16) ?: -1 shl 4) + text[i + 1].digitToIntOrNull(16)!! ?: -1).toByte()
            i += 2
        }
        return data
    }

    fun ConvertInt2Hex(decimal: Int): String {
        var decimal = decimal
        val digit = "0123456789abcdef"
        if (decimal <= 0) return ""
        val sysBase = 16 //основание системы счисления
        var xdigit = ""
        while (decimal > 0) {
            val dim = decimal % sysBase
            xdigit = digit[dim].toString() + xdigit
            decimal = decimal / sysBase
        }
        return "0x$xdigit"
    }

    fun ConvertHex2Int(hexadecimal: String): Int {
        return hexadecimal.toInt(16)
    }
}