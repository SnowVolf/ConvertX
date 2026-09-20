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
import ru.svolf.convertx.data.HistoryRecord
import ru.svolf.convertx.data.HistoryRepository
import ru.svolf.convertx.data.SettingsRepository
import ru.svolf.convertx.presentation.navigation.AppRoute
import ru.svolf.convertx.presentation.navigation.Base64Route
import ru.svolf.convertx.presentation.navigation.HexRoute
import javax.inject.Inject

private const val ConversionDebounceMs = 2_000L

data class ConverterUiState(
    val input: String = "",
    val output: String = "",
    val mode: Int = 0,
    val error: String? = null,
    val busy: Boolean = false,
    val conversionPulse: Long = 0L,
    val highlightInput: Boolean = false
)

class ConverterViewModel(
    private val route: AppRoute,
    private val history: HistoryRepository,
    private val settings: SettingsRepository,
    private val converter: ConverterEngine
) : ViewModel() {
    private val _state = MutableStateFlow(
        ConverterUiState(
            input = routeInput(route),
            output = routeOutput(route),
            mode = routeMode(route)
        )
    )
    val state: StateFlow<ConverterUiState> = _state.asStateFlow()
    private var conversionJob: Job? = null
    private var recordId = System.currentTimeMillis()

    init {
        viewModelScope.launch {
            settings.state.collect { value ->
                when (route) {
                    is Base64Route -> if (route.mode == 0) _state.update { it.copy(mode = value.base64Mode) }
                    is HexRoute -> if (route.mode == 0) _state.update { it.copy(mode = value.hexMode) }
                    else -> Unit
                }
            }
        }
    }

    fun onInputChanged(value: String) {
        _state.update { it.copy(input = value, error = null) }
        if (value.isEmpty()) recordId = System.currentTimeMillis()
        schedule { convertInput(value) }
    }

    fun onOutputChanged(value: String) {
        _state.update { it.copy(output = value, error = null) }
        if (value.isEmpty()) recordId = System.currentTimeMillis()
        schedule { convertOutput(value) }
    }

    fun onModeChanged(mode: Int) {
        _state.update { it.copy(mode = mode) }
        viewModelScope.launch {
            when (route) {
                is Base64Route -> settings.setBase64Mode(mode)
                is HexRoute -> settings.setHexMode(mode)
                else -> Unit
            }
        }
    }

    fun clear() {
        conversionJob?.cancel()
        recordId = System.currentTimeMillis()
        _state.value = _state.value.copy(input = "", output = "", error = null)
    }

    fun clearInput() {
        conversionJob?.cancel()
        recordId = System.currentTimeMillis()
        _state.update { it.copy(input = "", error = null) }
    }

    fun clearOutput() {
        conversionJob?.cancel()
        recordId = System.currentTimeMillis()
        _state.update { it.copy(output = "", error = null) }
    }

    private fun schedule(block: suspend () -> Unit) {
        conversionJob?.cancel()
        conversionJob = viewModelScope.launch {
            delay(ConversionDebounceMs)
            if (_state.value.input.isNotBlank() || _state.value.output.isNotBlank()) block()
        }
    }

    private suspend fun convertInput(value: String) = runConversion(value, true)

    private suspend fun convertOutput(value: String) {
        if (value.isNotBlank()) runConversion(value, false)
    }

    private suspend fun runConversion(value: String, fromInput: Boolean) {
        _state.update { it.copy(busy = true, error = null) }
        try {
            val result = withContext(Dispatchers.Default) {
                convert(value, fromInput, _state.value.mode)
            }
            val input = if (fromInput) value else result
            val output = if (fromInput) result else value
            _state.update {
                it.copy(
                    input = input,
                    output = output,
                    busy = false,
                    conversionPulse = it.conversionPulse + 1,
                    highlightInput = !fromInput
                )
            }
            if (decoder >= 0) {
                history.save(
                    HistoryRecord(
                        id = recordId,
                        decoder = decoder,
                        input = input,
                        output = output,
                        spinnerPosition = _state.value.mode
                    )
                )
            }
        } catch (error: IllegalArgumentException) {
            _state.update { it.copy(busy = false, error = error.message ?: "Conversion failed") }
        } catch (error: StringIndexOutOfBoundsException) {
            _state.update { it.copy(busy = false, error = error.message ?: "Conversion failed") }
        }
    }

    private val decoder: Int
        get() = converter.decoderFor(route)

    private suspend fun convert(value: String, fromInput: Boolean, mode: Int): String =
        converter.convert(route, value, fromInput, mode)

    class Factory @Inject constructor(
        private val history: HistoryRepository,
        private val settings: SettingsRepository,
        private val converter: ConverterEngine
    ) {
        fun forRoute(route: AppRoute): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    ConverterViewModel(route, history, settings, converter) as T
            }
    }

    companion object {
        private fun routeInput(route: AppRoute): String = when (route) {
            is Base64Route -> route.input.orEmpty()
            is HexRoute -> route.input.orEmpty()
            else -> ""
        }

        private fun routeOutput(route: AppRoute): String = when (route) {
            is Base64Route -> route.output.orEmpty()
            is HexRoute -> route.output.orEmpty()
            else -> ""
        }

        private fun routeMode(route: AppRoute): Int = when (route) {
            is Base64Route -> route.mode
            is HexRoute -> route.mode
            else -> 0
        }
    }
}
