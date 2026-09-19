package ru.svolf.convertx.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException
import javax.inject.Inject

data class RegexUiState(
    val expression: String = "",
    val sample: String = "",
    val result: String = "",
    val matches: List<IntRange> = emptyList(),
    val error: String? = null,
    val flags: Int = 0,
    val flagLabel: String = "/g"
)

class RegexViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(RegexUiState())
    val state: StateFlow<RegexUiState> = _state.asStateFlow()

    private val flagOptions = listOf(
        Pattern.CASE_INSENSITIVE to "i",
        Pattern.MULTILINE to "m",
        Pattern.COMMENTS to "x",
        Pattern.DOTALL to "s",
        Pattern.LITERAL to "-",
        Pattern.UNICODE_CASE to "u",
        Pattern.UNIX_LINES to "d"
    )

    fun setExpression(value: String) {
        _state.update { it.copy(expression = value) }
        evaluate()
    }

    fun setSample(value: String) {
        _state.update { it.copy(sample = value) }
        evaluate()
    }

    fun toggleFlag(index: Int) {
        val bit = flagOptions[index].first
        _state.update { current ->
            val flags =
                if (current.flags and bit == bit) current.flags and bit.inv() else current.flags or bit
            current.copy(
                flags = flags,
                flagLabel = "/g" + flagOptions.filter { flags and it.first == it.first }
                    .joinToString("") { it.second })
        }
        evaluate()
    }

    fun clear() {
        _state.value = RegexUiState(flags = _state.value.flags, flagLabel = _state.value.flagLabel)
    }

    fun flagSelected(index: Int): Boolean =
        _state.value.flags and flagOptions[index].first == flagOptions[index].first

    private fun evaluate() {
        val current = _state.value
        if (current.sample.isEmpty()) {
            _state.update { it.copy(result = "", matches = emptyList(), error = null) }
            return
        }
        if (current.expression.isEmpty()) {
            _state.update { it.copy(result = current.sample, matches = emptyList(), error = null) }
            return
        }
        try {
            val matcher = Pattern.compile(current.expression, current.flags).matcher(current.sample)
            val matches = buildList {
                while (matcher.find()) add(matcher.start() until matcher.end())
            }
            _state.update { it.copy(result = current.sample, matches = matches, error = null) }
        } catch (error: PatternSyntaxException) {
            _state.update { it.copy(result = "", matches = emptyList(), error = error.message) }
        }
    }
}
