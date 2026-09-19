package ru.svolf.convertx.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.svolf.convertx.data.AppDatabase
import ru.svolf.convertx.data.HistoryRepository
import ru.svolf.convertx.data.PaletteRepository
import ru.svolf.convertx.data.SettingsRepository
import ru.svolf.convertx.presentation.viewmodel.ConverterViewModel
import ru.svolf.convertx.presentation.viewmodel.HistoryViewModelFactory
import ru.svolf.convertx.presentation.viewmodel.PaletteViewModelFactory
import ru.svolf.convertx.presentation.viewmodel.RegexViewModelFactory
import ru.svolf.convertx.presentation.viewmodel.SettingsViewModelFactory
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class, DataStoreModule::class, RepositoryModule::class])
interface AppComponent {
    fun database(): AppDatabase
    fun historyRepository(): HistoryRepository
    fun settingsRepository(): SettingsRepository
    fun paletteRepository(): PaletteRepository
    fun converterViewModelFactory(): ConverterViewModel.Factory
    fun historyViewModelFactory(): HistoryViewModelFactory
    fun paletteViewModelFactory(): PaletteViewModelFactory
    fun settingsViewModelFactory(): SettingsViewModelFactory
    fun regexViewModelFactory(): RegexViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}
