package ru.svolf.convertx.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.svolf.convertx.data.HistoryRepository
import ru.svolf.convertx.data.PaletteRepository
import ru.svolf.convertx.data.SettingsRepository
import javax.inject.Inject

class HistoryViewModelFactory @Inject constructor(
    private val repository: HistoryRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = HistoryViewModel(repository) as T
}

class PaletteViewModelFactory @Inject constructor(
    private val repository: PaletteRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = PaletteViewModel(repository) as T
}

class SettingsViewModelFactory @Inject constructor(
    private val repository: SettingsRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        SettingsViewModel(repository) as T
}

class RegexViewModelFactory @Inject constructor() : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = RegexViewModel() as T
}
