package ru.svolf.convertx.data

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.svolf.convertx.data.entity.HistoryEntity

class HistoryRecordTest {
    @Test
    fun `legacy nullable row is mapped to a safe domain record`() {
        val record = HistoryEntity(
            id = 42L,
            decoder = null,
            input = null,
            output = null,
            spinnerPosition = null
        ).toRecord()

        assertEquals(42L, record.id)
        assertEquals(-1, record.decoder)
        assertEquals("", record.input)
        assertEquals("", record.output)
        assertEquals(0, record.spinnerPosition)
    }
}
