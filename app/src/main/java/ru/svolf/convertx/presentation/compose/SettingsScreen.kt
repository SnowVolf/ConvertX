package ru.svolf.convertx.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.svolf.convertx.R
import ru.svolf.convertx.data.SettingsState
import ru.svolf.convertx.data.ThemeMode
import ru.svolf.convertx.presentation.viewmodel.SettingsViewModel

@Composable
internal fun SettingsScreen(state: SettingsState, viewModel: SettingsViewModel) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text(
            stringResource(R.string.settings_ct_themes),
            style = MaterialTheme.typography.titleMedium
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChoice(stringResource(R.string.app_theme_light), state.theme == ThemeMode.LIGHT) {
                viewModel.setTheme(ThemeMode.LIGHT)
            }
            FilterChoice(stringResource(R.string.app_theme_dark), state.theme == ThemeMode.DARK) {
                viewModel.setTheme(ThemeMode.DARK)
            }
        }
        HorizontalDivider()
        Text(
            stringResource(R.string.settings_fontsize),
            style = MaterialTheme.typography.titleMedium
        )
        Text("${state.fontSize} sp")
        Slider(
            value = state.fontSize.toFloat(),
            onValueChange = { viewModel.setFontSize(it.toInt()) },
            valueRange = 12f..32f,
            steps = 19
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.settings_twicebtn), modifier = Modifier.weight(1f))
            Switch(checked = state.twiceBackToExit, onCheckedChange = viewModel::setTwiceBack)
        }
    }
}

@Composable
private fun FilterChoice(label: String, selected: Boolean, onClick: () -> Unit) {
    if (selected) Button(onClick = onClick) { Text(label) }
    else TextButton(onClick = onClick) { Text(label) }
}
