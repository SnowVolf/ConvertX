package ru.svolf.convertx.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.svolf.convertx.App
import ru.svolf.convertx.data.DbManager

/*
 * Created by SVolf on 26.01.2023, 15:10
 * This file is a part of "ConvertX" project
 */
@Module
class DatabaseModule {
    @Provides
    fun provideManager(): DbManager {
        return DbManager(App.instance as Context)
    }
}