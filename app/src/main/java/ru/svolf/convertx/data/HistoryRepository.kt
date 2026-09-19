package ru.svolf.convertx.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.svolf.convertx.data.dao.HistoryDao
import ru.svolf.convertx.data.entity.HistoryEntity

class HistoryRepository(private val dao: HistoryDao) {
    val records: Flow<List<HistoryRecord>> = dao.observeAll().map { rows ->
        rows.map { it.toRecord() }
    }

    suspend fun save(record: HistoryRecord) {
        dao.upsert(
            HistoryEntity(
                id = record.id,
                decoder = record.decoder,
                input = record.input,
                output = record.output,
                spinnerPosition = record.spinnerPosition
            )
        )
    }

    suspend fun find(id: Long): HistoryRecord? = dao.findById(id)?.toRecord()
    suspend fun delete(id: Long) = dao.deleteById(id)
    suspend fun deleteAll() = dao.deleteAll()
}
