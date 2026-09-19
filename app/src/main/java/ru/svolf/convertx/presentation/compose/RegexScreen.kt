package ru.svolf.convertx.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import ru.svolf.convertx.R
import ru.svolf.convertx.presentation.viewmodel.RegexUiState
import ru.svolf.convertx.presentation.viewmodel.RegexViewModel

@Composable
internal fun RegexScreen(state: RegexUiState, viewModel: RegexViewModel) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    Column(Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        TabRow(selectedTabIndex = tab) {
            Tab(
                selected = tab == 0,
                onClick = { tab = 0 },
                text = { Text(stringResource(R.string.tab_regex)) })
            Tab(
                selected = tab == 1,
                onClick = { tab = 1 },
                text = { Text(stringResource(R.string.tab_spur)) })
        }
        if (tab == 0) RegexValidator(state, viewModel) else SpurScreen()
    }
}

@Composable
private fun RegexValidator(state: RegexUiState, viewModel: RegexViewModel) {
    var flagsDialog by remember { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = state.expression,
            onValueChange = viewModel::setExpression,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.hint_regexp)) }
        )
        OutlinedTextField(
            value = state.sample,
            onValueChange = viewModel::setSample,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.hint_test_string)) }
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(state.matches.size.toString())
            AssistChip(onClick = { flagsDialog = true }, label = { Text(state.flagLabel) })
        }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        MatchText(state)
        if (flagsDialog) {
            AlertDialog(
                onDismissRequest = { flagsDialog = false },
                title = { Text(stringResource(R.string.regex_flags_title)) },
                text = {
                    Column {
                        listOf(
                            "Case insensitive [i]", "Multiline [m]", "Comments [x]",
                            "Dot all [s]", "Literal [-]", "Unicode Case [u]", "Unix Lines [d]"
                        ).forEachIndexed { index, label ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = viewModel.flagSelected(index),
                                    onCheckedChange = { viewModel.toggleFlag(index) }
                                )
                                Text(label)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        flagsDialog = false
                    }) { Text(stringResource(R.string.ok)) }
                }
            )
        }
    }
}

@Composable
private fun MatchText(state: RegexUiState) {
    val highlight = MaterialTheme.colorScheme.errorContainer
    val text = remember(state.result, state.matches) {
        buildAnnotatedString {
            append(state.result)
            state.matches.forEach { range ->
                if (range.first < range.last + 1) {
                    addStyle(SpanStyle(background = highlight), range.first, range.last + 1)
                }
            }
        }
    }
    Text(text, fontFamily = FontFamily.Monospace, modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp))
}

@Composable
private fun SpurScreen() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        text = context.assets.open("SPUR.md").bufferedReader().use { it.readText() }
    }
    androidx.compose.foundation.lazy.LazyColumn {
        item { Text(text, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(12.dp)) }
    }
}
