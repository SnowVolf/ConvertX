package ru.svolf.convertx.data.entity

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class PaletteTest {
    @Test
    fun `palette json is decoded into colors`() {
        val squash = Json.decodeFromString<Squash>(
            """{"palettes":[{"name":"Ocean","colors":[{"accent":"blue","hex":"#0000ff"}]}]}"""
        )

        assertEquals("Ocean", squash.palettes.single().name)
        assertEquals(Color("blue", "#0000ff"), squash.palettes.single().colors.single())
    }

    @Test
    fun `palette equality compares array contents`() {
        val first = Palette("One", arrayOf(Color("a", "#111111")))
        val sameColors = Palette("Two", arrayOf(Color("a", "#111111")))
        val differentColors = Palette("One", arrayOf(Color("b", "#222222")))

        assertEquals(first, sameColors)
        assertNotEquals(first, differentColors)
        assertEquals(first.hashCode(), sameColors.hashCode())
    }
}
