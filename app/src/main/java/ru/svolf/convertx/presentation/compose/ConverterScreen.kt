package ru.svolf.convertx.presentation.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.LocalAutofillHighlightBrush
import androidx.compose.foundation.text.LocalAutofillHighlightColor
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.svolf.convertx.R
import ru.svolf.convertx.presentation.navigation.AppRoute
import ru.svolf.convertx.presentation.navigation.Base64Route
import ru.svolf.convertx.presentation.navigation.HexRoute
import ru.svolf.convertx.presentation.navigation.TextTool
import ru.svolf.convertx.presentation.navigation.TextToolRoute
import ru.svolf.convertx.presentation.navigation.UnicodeRoute
import ru.svolf.convertx.presentation.viewmodel.ConverterUiState
import ru.svolf.convertx.presentation.viewmodel.ConverterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ConverterScreen(
    route: AppRoute,
    state: ConverterUiState,
    fontSize: Int,
    viewModel: ConverterViewModel,
    snackbar: SnackbarHostState
) {
    val copiedMessage = stringResource(R.string.copied2clipboard)
    val inputHint = when (route) {
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
    val outputHint = when (route) {
        UnicodeRoute -> stringResource(R.string.hint_unicode)
        is Base64Route -> stringResource(R.string.hint_base64)
        is HexRoute -> stringResource(R.string.hint_hex)
        else -> stringResource(R.string.copy2clipboard)
    }
    val textStyle = MaterialTheme.typography.bodyLarge.copy(
        fontFamily = FontFamily.Monospace,
        fontSize = fontSize.sp
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SwipeToClearTextField(
            value = state.input,
            onValueChange = viewModel::onInputChanged,
            onSwipeToClear = viewModel::clearInput,
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            label = inputHint,
            textStyle = textStyle,
            highlightPulse = if (state.highlightInput) state.conversionPulse else 0L,
            trailingIcon = {
                CopyButton(state.input, copiedMessage, snackbar)
            }
        )
        if (route is Base64Route || route is HexRoute) {
            ModeMenu(route, state.mode, viewModel::onModeChanged)
        }
        SwipeToClearTextField(
            value = state.output,
            onValueChange = viewModel::onOutputChanged,
            onSwipeToClear = viewModel::clearOutput,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
            minLines = 3,
            label = outputHint,
            textStyle = textStyle,
            highlightPulse = if (state.highlightInput) 0L else state.conversionPulse,
            trailingIcon = {
                CopyButton(state.output, copiedMessage, snackbar)
            }
        )
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}

@Composable
private fun SwipeToClearTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onSwipeToClear: () -> Unit,
    modifier: Modifier,
    minLines: Int,
    label: String,
    textStyle: TextStyle,
    highlightPulse: Long,
    trailingIcon: @Composable () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val hapticFeedback = LocalHapticFeedback.current
    val currentValue by rememberUpdatedState(value)
    val currentOnSwipeToClear by rememberUpdatedState(onSwipeToClear)
    val currentOnValueChange by rememberUpdatedState(onValueChange)
    val animationScope = rememberCoroutineScope()
    val textFieldState = rememberTextFieldState(value)
    var lastExternalValue by remember { mutableStateOf(value) }
    val density = androidx.compose.ui.platform.LocalDensity.current
    val maxSwipe = with(density) { 80.dp.toPx() }
    val swipeThreshold = with(density) { 56.dp.toPx() }
    val rainbowProgress = remember { Animatable(0f) }

    LaunchedEffect(highlightPulse) {
        if (highlightPulse != 0L) {
            rainbowProgress.snapTo(0f)
            rainbowProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 2_000, easing = FastOutSlowInEasing)
            )
        }
    }

    LaunchedEffect(value) {
        lastExternalValue = value
        if (textFieldState.text.toString() != value) {
            textFieldState.setTextAndPlaceCursorAtEnd(value)
        }
    }

    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text.toString() }.collectLatest { newValue ->
            if (newValue != lastExternalValue) {
                lastExternalValue = newValue
                currentOnValueChange(newValue)
            }
        }
    }

    val fieldShape = RoundedCornerShape(20.dp)
    val fieldColor = MaterialTheme.colorScheme.surfaceContainerHighest

    Box(
        modifier = modifier
            .graphicsLayer { translationX = offsetX.value }
            .shadow(elevation = 3.dp, shape = fieldShape)
            .clip(fieldShape)
            .background(fieldColor)
            .pointerInput(Unit) {
                var thresholdReached = false
                detectHorizontalDragGestures(
                    onDragStart = { thresholdReached = false },
                    onHorizontalDrag = { change, dragAmount ->
                        val nextOffset = (offsetX.value + dragAmount).coerceIn(-maxSwipe, 0f)
                        if (nextOffset != offsetX.value) {
                            change.consume()
                            animationScope.launch { offsetX.snapTo(nextOffset) }
                            if (!thresholdReached && nextOffset <= -swipeThreshold) {
                                thresholdReached = true
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        }
                    },
                    onDragEnd = {
                        if (thresholdReached && currentValue.isNotEmpty()) {
                            currentOnSwipeToClear()
                        }
                        animationScope.launch {
                            offsetX.animateTo(0f, tween(durationMillis = 220))
                        }
                        thresholdReached = false
                    },
                    onDragCancel = {
                        animationScope.launch {
                            offsetX.animateTo(0f, tween(durationMillis = 160))
                        }
                        thresholdReached = false
                    }
                )
            }
            .drawWithCache {
                val strokeWidth = 1.dp.toPx()
                val inset = strokeWidth / 2f
                val cornerRadius = 22.dp.toPx()
                onDrawWithContent {
                    drawContent()
                    val progress = rainbowProgress.value
                    val alpha = 1f - progress
                    if (highlightPulse != 0L && alpha > 0f) {
                        val colorPhase = progress * 360f
                        val rainbowBrush = Brush.sweepGradient(
                            colors = listOf(0f, 60f, 120f, 180f, 240f, 300f, 360f).map { hue ->
                                Color.hsv((hue - colorPhase + 360f) % 360f, 1f, 1f)
                            },
                            center = Offset(size.width / 2f, size.height / 2f)
                        )
                        drawRoundRect(
                            brush = rainbowBrush,
                            topLeft = Offset(inset, inset),
                            size = Size(size.width - strokeWidth, size.height - strokeWidth),
                            cornerRadius = CornerRadius(cornerRadius - inset),
                            style = Stroke(width = strokeWidth),
                            alpha = alpha
                        )
                    }
                }
            }
    ) {
        Box(Modifier.fillMaxWidth()) {
            CompositionLocalProvider(
                LocalAutofillHighlightBrush provides SolidColor(Color.Transparent),
                @Suppress("DEPRECATION")
                (LocalAutofillHighlightColor provides Color.Transparent)
            ) {
                BasicTextField(
                    state = textFieldState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 34.dp, end = 64.dp, bottom = 14.dp),
                    lineLimits = TextFieldLineLimits.MultiLine(minLines),
                    textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface)
                )
            }
            Text(
                text = label,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 12.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
            ) {
                trailingIcon()
            }
        }
    }
}

@Composable
private fun CopyButton(value: String, message: String, snackbar: SnackbarHostState) {
    val clipboard = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    IconButton(onClick = {
        clipboard.setText(AnnotatedString(value))
        scope.launch { snackbar.showSnackbar(message) }
    }) {
        Icon(
            Icons.Default.ContentCopy,
            contentDescription = stringResource(R.string.copy2clipboard)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModeMenu(route: AppRoute, selected: Int, onSelected: (Int) -> Unit) {
    val options = if (route is Base64Route) {
        listOf("No wrap", "No close", "No padding", "URL safe", "CRLF", "Default")
    } else {
        listOf("String", "Integer")
    }
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        AssistChip(
            onClick = { expanded = true },
            label = { Text(options.getOrElse(selected) { options.first() }) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor()
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { expanded = false; onSelected(index) },
                    leadingIcon = if (index == selected) ({
                        Icon(
                            Icons.Default.Check,
                            null
                        )
                    }) else null
                )
            }
        }
    }
}
