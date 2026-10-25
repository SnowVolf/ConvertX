package ru.svolf.convertx.presentation.compose

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ThemeChoice(stringResource(R.string.app_theme_light), state.theme == ThemeMode.LIGHT) {
                viewModel.setTheme(ThemeMode.LIGHT)
            }
            ThemeChoice(stringResource(R.string.app_theme_dark), state.theme == ThemeMode.DARK) {
                viewModel.setTheme(ThemeMode.DARK)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ThemeChoice(
                    stringResource(R.string.app_theme_system),
                    state.theme == ThemeMode.SYSTEM
                ) {
                    viewModel.setTheme(ThemeMode.SYSTEM)
                }
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
private fun ThemeChoice(label: String, selected: Boolean, onClick: () -> Unit) {
    val cardShape = RoundedCornerShape(14.dp)
    val indicatorShape = RoundedCornerShape(6.dp)
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(if (selected) colors.secondaryContainer else colors.surface)
            .border(
                width = 1.dp,
                color = if (selected) colors.primary else colors.outlineVariant,
                shape = cardShape
            )
            .selectable(selected = selected, onClick = onClick, role = Role.RadioButton)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(indicatorShape)
                .background(if (selected) colors.primary else colors.surface)
                .border(
                    width = 1.dp,
                    color = if (selected) colors.primary else colors.outline,
                    shape = indicatorShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = colors.onPrimary,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
        Spacer(Modifier.width(14.dp))
        Text(label, style = MaterialTheme.typography.titleSmall)
    }
}
