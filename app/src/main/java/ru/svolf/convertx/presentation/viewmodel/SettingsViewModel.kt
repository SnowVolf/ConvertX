package ru.svolf.convertx.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.svolf.convertx.data.SettingsRepository
import ru.svolf.convertx.data.ThemeMode
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {
    val state = repository.state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        ru.svolf.convertx.data.SettingsState()
    )

    fun setTheme(theme: ThemeMode) = viewModelScope.launch { repository.setTheme(theme) }
    fun setFontSize(size: Int) = viewModelScope.launch { repository.setFontSize(size) }
    fun setTwiceBack(enabled: Boolean) =
        viewModelScope.launch { repository.setTwiceBackToExit(enabled) }
}
