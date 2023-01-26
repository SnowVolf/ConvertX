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
    @Query("SELECT * FROM History")
    fun getAll(): LiveData<List<HistoryItem>>

    @Delete
    fun delete(item: HistoryItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: HistoryItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: HistoryItem)
}