package ru.svolf.convertx.data

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.svolf.convertx.presentation.navigation.AboutRoute
import ru.svolf.convertx.presentation.navigation.Base64Route
import ru.svolf.convertx.presentation.navigation.HexRoute
import ru.svolf.convertx.presentation.navigation.RegexRoute
import ru.svolf.convertx.presentation.navigation.TextTool
import ru.svolf.convertx.presentation.navigation.TextToolRoute
import ru.svolf.convertx.presentation.navigation.UnicodeRoute
import ru.svolf.convertx.utils.DecoderConst
import java.util.zip.Adler32
import java.util.zip.CRC32

class ConverterEngineTest {
    private val engine = ConverterEngine()

    @Test
    fun `decoder is selected by route`() {
        assertEquals(DecoderConst.UNICODE, engine.decoderFor(UnicodeRoute))
        assertEquals(DecoderConst.BASE64, engine.decoderFor(Base64Route()))
        assertEquals(DecoderConst.HEX, engine.decoderFor(HexRoute()))
        assertEquals(-1, engine.decoderFor(RegexRoute))
    }

    @Test
    fun `unicode route converts in both directions`() = runTest {
        assertEquals(
            "\\u0041\\u03a9",
            engine.convert(UnicodeRoute, "AΩ", fromInput = true, mode = 0)
        )
        assertEquals(
            "AΩ",
            engine.convert(UnicodeRoute, "\\u0041\\u03a9", fromInput = false, mode = 0)
        )
    }

    @Test
    fun `base64 route clamps mode and converts in both directions`() = runTest {
        val encoded = engine.convert(Base64Route(), "hello", fromInput = true, mode = 99)

        assertEquals("hello", engine.convert(Base64Route(), encoded, fromInput = false, mode = 99))
        assertEquals("aGVsbG8=", encoded)
    }

    @Test
    fun `hex route supports string and integer modes`() = runTest {
        assertEquals("Hi", engine.convert(HexRoute(), "4869", fromInput = false, mode = 0))
        assertEquals(
            "0x0000000000000000000000000000000000004869",
            engine.convert(HexRoute(), "Hi", fromInput = true, mode = 0)
        )
        assertEquals("0xff", engine.convert(HexRoute(), "255", fromInput = true, mode = 1))
        assertEquals("255", engine.convert(HexRoute(), "0xff", fromInput = false, mode = 1))
    }

    @Test
    fun `text tools calculate checksums and decode xml`() = runTest {
        val input = "hello"
        val adler = Adler32().apply { update(input.toByteArray()) }.value.toString()
        val crc = CRC32().apply { update(input.toByteArray()) }.value.toString()

        assertEquals(adler, engine.convert(TextToolRoute(TextTool.ADLER32), input, true, 0))
        assertEquals(crc, engine.convert(TextToolRoute(TextTool.CRC32), input, true, 0))
        assertEquals(
            "<b>ok</b>",
            engine.convert(TextToolRoute(TextTool.XML), "&lt;b&gt;ok&lt;/b&gt;", true, 0)
        )
    }

    @Test
    fun `unsupported routes pass the input through`() = runTest {
        assertEquals("same", engine.convert(AboutRoute, "same", true, 0))
    }
}
