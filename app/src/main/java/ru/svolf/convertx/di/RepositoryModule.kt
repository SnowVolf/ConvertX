package ru.svolf.convertx.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import ru.svolf.convertx.data.ConverterEngine
import ru.svolf.convertx.data.HistoryRepository
import ru.svolf.convertx.data.PaletteRepository
import ru.svolf.convertx.data.SettingsRepository
import ru.svolf.convertx.data.dao.HistoryDao
import javax.inject.Singleton

@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideConverterEngine() = ConverterEngine()

    @Provides
    @Singleton
    fun provideHistoryRepository(dao: HistoryDao) = HistoryRepository(dao)

    @Provides
    @Singleton
    fun provideSettingsRepository(dataStore: DataStore<Preferences>) = SettingsRepository(dataStore)

    @Provides
    @Singleton
    fun providePaletteRepository(context: android.content.Context) = PaletteRepository(context)
}
