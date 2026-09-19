package ru.svolf.convertx.presentation.navigation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import ru.svolf.convertx.data.HistoryRecord

class HistoryRouteMapperTest {
    @Test
    fun `known decoder ids map to converter routes`() {
        assertEquals(UnicodeRoute, HistoryRecord(1, 0, "i", "o", 0).toRoute())
        assertEquals(Base64Route("i", "o", 4), HistoryRecord(2, 1, "i", "o", 4).toRoute())
        assertEquals(HexRoute("i", "o", 1), HistoryRecord(3, 2, "i", "o", 1).toRoute())
    }

    @Test
    fun `unknown decoder ids are not navigable`() {
        assertNull(HistoryRecord(1, -1, "", "", 0).toRoute())
        assertNull(HistoryRecord(1, 99, "", "", 0).toRoute())
    }
}
