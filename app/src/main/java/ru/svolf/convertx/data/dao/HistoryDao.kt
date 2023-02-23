package ru.svolf.convertx.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.svolf.convertx.data.entity.HistoryItem

/*
 * Created by SVolf on 26.01.2023, 14:32
 * This file is a part of "ConvertX" project
 */
@Dao
interface HistoryDao {
    @Query("SELECT * FROM History ORDER BY id DESC")
    fun getAll(): LiveData<List<HistoryItem>>

    @Query("SELECT * FROM History WHERE id=:id")
    fun getById(id: Long): HistoryItem

    @Query("DELETE FROM History")
    fun deleteAll()

    @Delete
    fun delete(vararg items: HistoryItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: HistoryItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: HistoryItem)
}