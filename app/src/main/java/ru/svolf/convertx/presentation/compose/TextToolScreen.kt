package ru.svolf.convertx.presentation.compose

import android.content.ClipData
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.svolf.convertx.R
import ru.svolf.convertx.presentation.navigation.TextToolRoute
import ru.svolf.convertx.presentation.viewmodel.TextToolUiState
import ru.svolf.convertx.presentation.viewmodel.TextToolViewModel

@Composable
internal fun TextToolScreen(
    route: TextToolRoute,
    state: TextToolUiState,
    fontSize: Int,
    viewModel: TextToolViewModel,
    snackbar: SnackbarHostState,
    setToolbarActions: (@Composable androidx.compose.foundation.layout.RowScope.() -> Unit) -> Unit
) {
    val textStyle = MaterialTheme.typography.bodyLarge.copy(
        fontFamily = FontFamily.Monospace, fontSize = fontSize.sp
    )
    val copiedMessage = stringResource(R.string.copied2clipboard)
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.output, setToolbarActions) {
        setToolbarActions {
            TextToolToolbarActions(
                route = route,
                output = state.output,
                copiedMessage = copiedMessage,
                snackbar = snackbar,
                onInsertTimestamp = {
                    val currentTimestamp = System.currentTimeMillis().toString()
                    viewModel.onInputChanged(currentTimestamp)
                }
            )
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            setToolbarActions {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SwipeToClearTextField(
            value = state.input,
            onValueChange = viewModel::onInputChanged,
            onSwipeToClear = { viewModel.onInputChanged("") },
            isActive = true,
            onFocused = { },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5,
            label = inputHint(route),
            textStyle = textStyle,
            highlightPulse = 0L,
            copyAction = {
                CopyButton(state.input, copiedMessage, snackbar)
            }
        )
        
        if (state.output.isNotEmpty()) {
            TextToolOutputCard(state.output, textStyle)
        }
        
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}

@Composable
internal fun TextToolToolbarActions(
    route: TextToolRoute,
    output: String,
    copiedMessage: String,
    snackbar: SnackbarHostState,
    onInsertTimestamp: () -> Unit
) {
    val clipboard = LocalClipboard.current
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    androidx.compose.material3.Card(
        modifier = Modifier.height(46.dp),
        shape = androidx.compose.foundation.shape.CircleShape,
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        androidx.compose.foundation.layout.Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            if (route.tool == ru.svolf.convertx.presentation.navigation.TextTool.TIMESTAMP) {
                IconButton(onClick = onInsertTimestamp) {
                    Icon(
                        imageVector = Icons.Default.AdsClick,
                        contentDescription = "Insert Timestamp"
                    )
                }
            }

            IconButton(
                onClick = {
                    if (output.isNotEmpty()) {
                        scope.launch {
                            val clipData = ClipData.newPlainText(null, output)
                            clipboard.setClipEntry(ClipEntry(clipData))
                            snackbar.showSnackbar(copiedMessage)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = stringResource(R.string.copy2clipboard)
                )
            }
        }
    }
}

@Composable
internal fun TextToolOutputCard(output: String, textStyle: androidx.compose.ui.text.TextStyle) {
    androidx.compose.material3.OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = androidx.compose.material3.CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        androidx.compose.foundation.text.selection.SelectionContainer {
            Text(
                text = output,
                modifier = Modifier.padding(16.dp),
                style = textStyle,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
