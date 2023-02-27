package ru.svolf.convertx.utils.algorhitms

import android.util.Base64
import kotlinx.coroutines.coroutineScope
import java.math.BigInteger
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Snow Volf on 21.02.2017, 8:42
 */
object Decoder {
	/**
	 * Расшифровка Unicode
	 */
	suspend fun decodeUnicode(tracer: String): String {
		return coroutineScope {
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
						when (aChar) {
							't' -> aChar = '\t'
							'r' -> aChar = '\r'
							'n' -> aChar =
								'\n'
							'f' -> aChar = '\u000c'
						}
						outBuffer.append(aChar)
					}
				} else outBuffer.append(aChar)
			}
			outBuffer.toString()
		}

	}

	/**
	 * Расшифровка XML
	 */
	suspend fun unescapeXml(xml: String): String {
		return suspendCoroutine {
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
				if (hashmark != null && hashmark.isNotEmpty()) {
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
			unescapedOutput.toString()
		}
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
	suspend fun getNormalDate(time: Long): String {
		return coroutineScope {
			val date = Date(time)
			val format: Format = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH)
			format.format(date)
		}
	}

	/*
	* Декодирование hex текста
	*/
	suspend fun decodeHexString(hexString: String): String {
		return coroutineScope {
			val str = StringBuilder()
			var i = 0
			while (i < hexString.length) {
				val s = hexString.substring(i, i + 2)
				val decimal = s.toInt(16)
				str.append(decimal.toChar())
				i += 2
			}
			str.toString()
		}
	}

	/*
	* Кодирование текста в hex
	*/
	suspend fun toHexString(text: String): String {
		return coroutineScope {
			"0x".plus(String.format("%040X", BigInteger(1, text.toByteArray())))
		}

	}

	/*
	* Декодирование hex в число (int)
	*/
	suspend fun hexToInt(hexadecimal: String): String {
		return suspendCoroutine {
			try {
				val test = hexadecimal.toInt(16).toString().replaceFirst("0x", "")
				it.resume(test)
			} catch (ex: Throwable) {
				it.resume("Error. Out of range")
			}
		}
	}

	/*
	 * Декодирование int в hex
	 */
	suspend fun intToHex(decimal: String): String {
		return suspendCoroutine {
			try {
				val xdigit = Integer.toHexString(decimal.toInt())
				it.resume("0x$xdigit")
			} catch (ex: Throwable) {
				it.resume("Error. Out of range")
			}
		}
	}

	suspend fun decodeBase64(text: String, mode: Int): String {
		return coroutineScope {
			val dBytes = Base64.decode(text.toByteArray(), mode)
			String(dBytes, Charsets.UTF_8)
		}
	}

	suspend fun encodeBase64(text: String, mode: Int): String {
		return coroutineScope {
			val eBytes = Base64.encode(text.toByteArray(), mode)
			String(eBytes, Charsets.UTF_8)
		}
	}

}