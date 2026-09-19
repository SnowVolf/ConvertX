package ru.svolf.convertx

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import ru.svolf.convertx.data.dao.HistoryDao
import ru.svolf.convertx.data.entity.HistoryEntity

internal class InMemoryDataStore(
    initial: Preferences = emptyPreferences()
) : DataStore<Preferences> {
    private val values = MutableStateFlow(initial)

    override val data: Flow<Preferences> = values

    override suspend fun updateData(transform: suspend (Preferences) -> Preferences): Preferences {
        val updated = transform(values.value)
        values.value = updated
        return updated
    }
}

internal class InMemoryHistoryDao : HistoryDao {
    private val rows = MutableStateFlow<List<HistoryEntity>>(emptyList())

    override fun observeAll(): Flow<List<HistoryEntity>> = rows

    override suspend fun findById(id: Long): HistoryEntity? = rows.value.firstOrNull { it.id == id }

    override suspend fun upsert(entity: HistoryEntity) {
        rows.value = (rows.value.filterNot { it.id == entity.id } + entity)
            .sortedByDescending { it.id }
    }

    override suspend fun deleteById(id: Long) {
        rows.value = rows.value.filterNot { it.id == id }
    }

    override suspend fun deleteAll() {
        rows.value = emptyList()
    }

    suspend fun seed(vararg entities: HistoryEntity) {
        entities.forEach { upsert(it) }
    }
}

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
internal fun runViewModelTest(block: suspend TestScope.() -> Unit) = runTest {
    Dispatchers.setMain(StandardTestDispatcher(testScheduler))
    try {
        block()
        advanceUntilIdle()
    } finally {
        Dispatchers.resetMain()
    }
}
