package ru.svolf.convertx.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.svolf.convertx.data.ConverterEngine
import ru.svolf.convertx.presentation.navigation.TextToolRoute
import javax.inject.Inject

data class TextToolUiState(
    val input: String = "",
    val output: String = "",
    val error: String? = null,
    val busy: Boolean = false
)

class TextToolViewModel(
    private val route: TextToolRoute,
    private val converter: ConverterEngine
) : ViewModel() {
    private val _state = MutableStateFlow(TextToolUiState())
    val state: StateFlow<TextToolUiState> = _state.asStateFlow()
    private var conversionJob: Job? = null

    fun onInputChanged(value: String) {
        _state.update { it.copy(input = value, error = null) }
        schedule { runConversion(value) }
    }

    private fun schedule(block: suspend () -> Unit) {
        conversionJob?.cancel()
        conversionJob = viewModelScope.launch {
            delay(DEBOUNCE_DELAY)
            if (_state.value.input.isNotBlank()) block()
            else _state.update { it.copy(output = "") }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun runConversion(value: String) {
        _state.update { it.copy(busy = true, error = null) }
        try {
            val result = withContext(Dispatchers.Default) {
                converter.convert(route, value, true, 0)
            }
            _state.update {
                it.copy(output = result, busy = false)
            }
        } catch (error: Exception) {
            _state.update { it.copy(busy = false, error = error.message ?: "Conversion failed") }
        }
    }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
    }

    class Factory @Inject constructor(
        private val converter: ConverterEngine
    ) {
        fun forRoute(route: TextToolRoute): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    TextToolViewModel(route, converter) as T
            }
    }
}
