package ru.svolf.convertx.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.svolf.convertx.data.HistoryRecord
import ru.svolf.convertx.data.HistoryRepository
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {
    val records = repository.records.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )

    fun delete(record: HistoryRecord) = viewModelScope.launch { repository.delete(record.id) }
    fun clearAll() = viewModelScope.launch { repository.deleteAll() }
}
