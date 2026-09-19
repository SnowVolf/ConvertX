package ru.svolf.convertx.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.svolf.convertx.data.AppDatabase
import ru.svolf.convertx.data.dao.HistoryDao
import javax.inject.Singleton

@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "ConvertX_Db").build()

    @Provides
    fun provideHistoryDao(database: AppDatabase): HistoryDao = database.historyDao()
}
