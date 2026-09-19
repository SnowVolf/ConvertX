package ru.svolf.convertx.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.svolf.convertx.data.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Query("SELECT * FROM History ORDER BY id DESC")
    fun observeAll(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM History WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): HistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: HistoryEntity)

    @Query("DELETE FROM History WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM History")
    suspend fun deleteAll()
}
