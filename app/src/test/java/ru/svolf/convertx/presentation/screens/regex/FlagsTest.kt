package ru.svolf.convertx.presentation.screens.regex

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.regex.Pattern


class FlagsTest {
//    @Test
//    fun testAdd() {
//        val flags = Flags()
//        flags.add(0)
//        flags.add(5)
//        flags.add(6)
//        assertEquals(flags.flags, Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE or Pattern.UNIX_LINES)
//    }

    @Test
    fun testRemove() {
        val flags = Flags()
        flags.add(0)
        flags.add(5)
        flags.add(6)
        flags.remove(0)
        flags.remove(5)
        assertEquals(flags.flags, Pattern.UNIX_LINES)
    }

    @Test
    fun testSelectedBooleans() {
        val flags = Flags()
        flags.add(0)
        flags.add(5)
        flags.add(6)
        val expected = booleanArrayOf(true, false, false, false, false, true, true)
        assertArrayEquals(flags.selectedBooleans, expected)
    }

    @Test
    fun testFlagString() {
        val flags = Flags()
        flags.add(0)
        flags.add(5)
        flags.add(6)
        assertEquals(flags.flagString, "/giud")
    }
}