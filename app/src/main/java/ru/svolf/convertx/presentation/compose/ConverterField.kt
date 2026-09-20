package ru.svolf.convertx.presentation.compose

import android.content.ClipData
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.svolf.convertx.R
import ru.svolf.convertx.presentation.navigation.AppRoute
import ru.svolf.convertx.presentation.navigation.Base64Route
import ru.svolf.convertx.presentation.navigation.HexRoute
import ru.svolf.convertx.presentation.navigation.TextTool
import ru.svolf.convertx.presentation.navigation.TextToolRoute
import ru.svolf.convertx.presentation.navigation.UnicodeRoute

internal enum class ConverterField { INPUT, OUTPUT }

@Composable
internal fun inputHint(route: AppRoute): String = when (route) {
    UnicodeRoute, is Base64Route -> stringResource(R.string.hint_utf)
    is HexRoute -> stringResource(R.string.hint_string)
    is TextToolRoute -> when (route.tool) {
        TextTool.ADLER32 -> stringResource(R.string.hint_adler32)
        TextTool.CRC32 -> stringResource(R.string.hint_crc)
        TextTool.XML -> stringResource(R.string.hint_encoded_xml)
        TextTool.TIMESTAMP -> stringResource(R.string.hint_timestamp)
    }

    else -> stringResource(R.string.hint_string)
}

@Composable
internal fun outputHint(route: AppRoute): String = when (route) {
    UnicodeRoute -> stringResource(R.string.hint_unicode)
    is Base64Route -> stringResource(R.string.hint_base64)
    is HexRoute -> stringResource(R.string.hint_hex)
    else -> stringResource(R.string.copy2clipboard)
}

@Composable
internal fun ConverterControls(
    route: AppRoute,
    selectedMode: Int,
    activeField: ConverterField,
    onFieldToggle: () -> Unit,
    onModeSelected: (Int) -> Unit
) {
    Box(Modifier.fillMaxWidth().height(48.dp)) {
        DirectionSwitcher(activeField, onFieldToggle, Modifier.align(Alignment.Center))
        if (route is Base64Route || route is ru.svolf.convertx.presentation.navigation.HexRoute) {
            ModeMenu(route, selectedMode, onModeSelected, Modifier.align(Alignment.CenterStart))
        }
    }
}

@Composable
private fun DirectionSwitcher(
    activeField: ConverterField,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary
    val muted = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
    val downTint by animateColorAsState(
        targetValue = if (activeField == ConverterField.INPUT) primary else muted,
        animationSpec = tween(durationMillis = 220),
        label = "downArrowTint"
    )
    val upTint by animateColorAsState(
        targetValue = if (activeField == ConverterField.OUTPUT) primary else muted,
        animationSpec = tween(durationMillis = 220),
        label = "upArrowTint"
    )
    Column(
        modifier = modifier.height(46.dp).clickable(onClick = onToggle),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.width(2.dp).weight(1f).background(primary.copy(alpha = 0.65f)))
        Row(horizontalArrangement = Arrangement.Center) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = downTint,
                modifier = Modifier.size(20.dp)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                tint = upTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Box(Modifier.width(2.dp).weight(1f).background(primary.copy(alpha = 0.65f)))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModeMenu(
    route: AppRoute,
    selected: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = if (route is Base64Route) {
        listOf("No wrap", "No close", "No padding", "URL safe", "CRLF", "Default")
    } else {
        listOf("String", "Integer")
    }
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded, { expanded = !expanded }, modifier) {
        AssistChip(
            onClick = { expanded = true },
            label = { Text(options.getOrElse(selected) { options.first() }) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor()
        )
        DropdownMenu(expanded, { expanded = false }) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { expanded = false; onSelected(index) },
                    leadingIcon = if (index == selected) {
                        { Icon(Icons.Default.Check, null) }
                    } else {
                        null
                    }
                )
            }
        }
    }
}

@Composable
internal fun ClearButton(onClick: () -> Unit) {
    TextButton(modifier = Modifier.height(48.dp), onClick = onClick) {
        Icon(Icons.Default.Clear, stringResource(R.string.clear))
        Spacer(Modifier.width(8.dp))
        Text(stringResource(R.string.clear))
    }
}

@Composable
internal fun CopyButton(value: String, message: String, snackbar: SnackbarHostState) {
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    TextButton(
        modifier = Modifier.height(48.dp),
        onClick = {
            scope.launch {
                clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(null, value)))
                snackbar.showSnackbar(message)
            }
        }
    ) {
        Icon(Icons.Default.ContentCopy, stringResource(R.string.copy2clipboard))
        Spacer(Modifier.width(8.dp))
        Text(stringResource(R.string.copy2clipboard))
    }
}
