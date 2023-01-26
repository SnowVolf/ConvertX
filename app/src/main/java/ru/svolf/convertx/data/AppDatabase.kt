package ru.svolf.convertx.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.svolf.convertx.data.dao.HistoryDao
import ru.svolf.convertx.data.entity.HistoryItem

/*
 * Created by SVolf on 26.01.2023, 15:14
 * This file is a part of "ConvertX" project
 */
@Database(entities = [HistoryItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}