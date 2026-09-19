package ru.svolf.convertx.utils.algorhitms

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DecoderTest {
    @Test
    fun `unicode escapes and java escapes are decoded`() = runTest {
        assertEquals("AΩ\n\t", Decoder.decodeUnicode("\\u0041\\u03A9\\n\\t"))
        assertEquals("plain text", Decoder.decodeUnicode("plain text"))
    }

    @Test
    fun `malformed unicode digit is rejected`() = runTest {
        val error = runCatching { Decoder.decodeUnicode("\\u12x4") }.exceptionOrNull()

        assertTrue(error is IllegalArgumentException)
    }

    @Test
    fun `xml entities are unescaped and unknown entities are preserved`() = runTest {
        assertEquals(
            "<tag a='1'>&\"x\"</tag> ✏ &unknown;",
            Decoder.unescapeXml("&lt;tag a=&apos;1&apos;&gt;&amp;&quot;x&quot;&lt;/tag&gt; &#9999; &unknown;")
        )
    }

    @Test
    fun `hex string is encoded and decoded`() = runTest {
        val encoded = Decoder.toHexString("Hi")

        assertTrue(encoded.startsWith("0x"))
        assertTrue(encoded.endsWith("4869"))
        assertEquals(42, encoded.length)
        assertEquals("Hi", Decoder.decodeHexString("4869"))
    }

    @Test
    fun `integer conversion returns hexadecimal and handles overflow`() = runTest {
        assertEquals("0xff", Decoder.intToHex("255"))
        assertEquals("255", Decoder.hexToInt("ff"))
        assertEquals("Error. Out of range", Decoder.intToHex("not-a-number"))
        assertEquals("Error. Out of range", Decoder.hexToInt("fffffffff"))
    }

    @Test
    fun `base64 round trip supports standard and url safe data`() = runTest {
        val plain = "hello?"
        val encoded = Decoder.encodeBase64(plain, android.util.Base64.NO_WRAP)

        assertEquals(plain, Decoder.decodeBase64(encoded, android.util.Base64.NO_WRAP))

        val urlEncoded = Decoder.encodeBase64(
            "??",
            android.util.Base64.URL_SAFE or android.util.Base64.NO_PADDING
        )
        assertFalse(urlEncoded.contains("="))
        assertEquals("??", Decoder.decodeBase64(urlEncoded, android.util.Base64.URL_SAFE))
    }

    @Test
    fun `timestamp conversion uses a stable english date format`() = runTest {
        val result = Decoder.getNormalDate(0L)

        assertTrue(result.matches(Regex("\\d{2}\\.\\d{2}\\.1970 \\d{2}:\\d{2}:\\d{2}")))
    }
}
