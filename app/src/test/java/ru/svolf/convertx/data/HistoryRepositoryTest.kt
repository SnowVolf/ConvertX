package ru.svolf.convertx.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import ru.svolf.convertx.InMemoryHistoryDao

class HistoryRepositoryTest {
    @Test
    fun `records are mapped from dao rows`() = runTest {
        val dao = InMemoryHistoryDao()
        dao.seed(
            HistoryRecord(2L, 1, "a", "b", 0).toEntity(),
            HistoryRecord(1L, 0, "c", "d", 0).toEntity()
        )
        val repository = HistoryRepository(dao)

        assertEquals(listOf(2L, 1L), repository.records.first().map { it.id })
        assertEquals("b", repository.records.first().first().output)
    }

    @Test
    fun `save and find preserve every history field`() = runTest {
        val repository = HistoryRepository(InMemoryHistoryDao())
        val expected = HistoryRecord(10L, 2, "input", "output", 1)

        repository.save(expected)

        assertEquals(expected, repository.find(10L))
    }

    @Test
    fun `delete removes one record and deleteAll clears history`() = runTest {
        val dao = InMemoryHistoryDao()
        val repository = HistoryRepository(dao)
        repository.save(HistoryRecord(1L, 0, "a", "b", 0))
        repository.save(HistoryRecord(2L, 1, "c", "d", 2))

        repository.delete(1L)
        assertNull(repository.find(1L))
        assertEquals(listOf(2L), repository.records.first().map { it.id })

        repository.deleteAll()
        assertEquals(emptyList<HistoryRecord>(), repository.records.first())
    }

    private fun HistoryRecord.toEntity() = ru.svolf.convertx.data.entity.HistoryEntity(
        id = id,
        decoder = decoder,
        input = input,
        output = output,
        spinnerPosition = spinnerPosition
    )
}
