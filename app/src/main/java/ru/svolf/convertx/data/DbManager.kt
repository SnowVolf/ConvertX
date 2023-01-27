package ru.svolf.convertx.data

import android.content.Context
import androidx.room.Room

/*
 * Created by SVolf on 26.01.2023, 15:13
 * This file is a part of "ConvertX" project
 */
class DbManager(context: Context) {
    private var appDatabase: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "ConvertX_Db").build()

    fun getDatabase(): AppDatabase {
        return appDatabase
    }
}