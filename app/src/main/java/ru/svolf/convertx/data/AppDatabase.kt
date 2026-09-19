package ru.svolf.convertx.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.svolf.convertx.data.dao.HistoryDao
import ru.svolf.convertx.data.entity.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}
