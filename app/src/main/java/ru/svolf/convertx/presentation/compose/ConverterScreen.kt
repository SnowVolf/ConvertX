package ru.svolf.convertx.presentation.compose

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.LocalAutofillHighlightBrush
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.svolf.convertx.R
import ru.svolf.convertx.presentation.navigation.AppRoute
import ru.svolf.convertx.presentation.viewmodel.ConverterUiState
import ru.svolf.convertx.presentation.viewmodel.ConverterViewModel

private const val ClearSoundVolume = 0.55f

internal data class SwipeToClearState(
    val offsetX: Animatable<Float, AnimationVector1D>,
    val hapticFeedback: HapticFeedback,
    val context: Context,
    val currentValue: String,
    val currentOnSwipeToClear: () -> Unit,
    val animationScope: CoroutineScope,
    val textFieldState: androidx.compose.foundation.text.input.TextFieldState,
    val maxSwipe: Float,
    val swipeThreshold: Float,
    val rainbowProgress: Animatable<Float, AnimationVector1D>,
    val activeBorderAlpha: Float,
    val fieldShape: Shape,
    val fieldColor: Color,
    val activeBorderColor: Color
)

@Composable
internal fun ConverterScreen(
    route: AppRoute,
    state: ConverterUiState,
    fontSize: Int,
    viewModel: ConverterViewModel,
    snackbar: SnackbarHostState
) {
    val copiedMessage = stringResource(R.string.copied2clipboard)
    val inputHint = inputHint(route)
    val outputHint = outputHint(route)
    val textStyle = MaterialTheme.typography.bodyLarge.copy(
        fontFamily = FontFamily.Monospace, fontSize = fontSize.sp
    )
    var activeField by remember { mutableStateOf(ConverterField.INPUT) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        ConverterTextField(
            value = state.input,
            onValueChange = viewModel::onInputChanged,
            onSwipeToClear = viewModel::clearInput,
            isActive = activeField == ConverterField.INPUT,
            onFocused = { activeField = ConverterField.INPUT },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            label = inputHint,
            textStyle = textStyle,
            highlightPulse = if (state.highlightInput) state.conversionPulse else 0L,
            copyAction = {
                CopyButton(state.input, copiedMessage, snackbar)
            }
        )
        ConverterControls(
            route = route,
            selectedMode = state.mode,
            activeField = activeField,
            onFieldToggle = {
                activeField = when (activeField) {
                    ConverterField.INPUT -> ConverterField.OUTPUT
                    ConverterField.OUTPUT -> ConverterField.INPUT
                }
            },
            onModeSelected = viewModel::onModeChanged
        )
        ConverterTextField(
            value = state.output,
            onValueChange = viewModel::onOutputChanged,
            onSwipeToClear = viewModel::clearOutput,
            isActive = activeField == ConverterField.OUTPUT,
            onFocused = { activeField = ConverterField.OUTPUT },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            label = outputHint,
            textStyle = textStyle,
            highlightPulse = if (state.highlightInput) 0L else state.conversionPulse,
            copyAction = {
                CopyButton(state.output, copiedMessage, snackbar)
            }
        )
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}

@Composable
private fun ConverterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onSwipeToClear: () -> Unit,
    isActive: Boolean,
    onFocused: () -> Unit,
    modifier: Modifier,
    minLines: Int,
    label: String,
    textStyle: TextStyle,
    highlightPulse: Long,
    copyAction: @Composable () -> Unit
) {
    SwipeToClearTextField(
        value = value,
        onValueChange = onValueChange,
        onSwipeToClear = onSwipeToClear,
        isActive = isActive,
        onFocused = onFocused,
        modifier = modifier,
        minLines = minLines,
        label = label,
        textStyle = textStyle,
        highlightPulse = highlightPulse,
        copyAction = copyAction
    )
}

@Composable
internal fun SwipeToClearTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onSwipeToClear: () -> Unit,
    isActive: Boolean,
    onFocused: () -> Unit,
    modifier: Modifier,
    minLines: Int,
    label: String,
    textStyle: TextStyle,
    highlightPulse: Long,
    copyAction: @Composable () -> Unit
) {
    val fieldState = rememberSwipeToClearState(
        value = value,
        onValueChange = onValueChange,
        onSwipeToClear = onSwipeToClear,
        isActive = isActive,
        highlightPulse = highlightPulse,
    )

    Box(
        modifier = modifier
            .graphicsLayer { translationX = fieldState.offsetX.value }
            .shadow(elevation = 3.dp, shape = fieldState.fieldShape)
            .clip(fieldState.fieldShape)
            .background(fieldState.fieldColor)
            .clearSwipeGestures(
                offsetX = fieldState.offsetX,
                maxSwipe = fieldState.maxSwipe,
                swipeThreshold = fieldState.swipeThreshold,
                currentValue = fieldState.currentValue,
                currentOnSwipeToClear = fieldState.currentOnSwipeToClear,
                context = fieldState.context,
                hapticFeedback = fieldState.hapticFeedback,
                animationScope = fieldState.animationScope
            )
            .drawSwipeBorder(
                activeBorderAlpha = fieldState.activeBorderAlpha,
                activeBorderColor = fieldState.activeBorderColor,
                rainbowProgress = fieldState.rainbowProgress.value,
                highlightPulse = highlightPulse
            )
    ) {
        SwipeToClearFieldContent(
            textFieldState = fieldState.textFieldState,
            onFocused = onFocused,
            minLines = minLines,
            textStyle = textStyle,
            label = label,
            onClear = {
                animateClearFromButton(
                    fieldState.currentValue,
                    fieldState.hapticFeedback,
                    fieldState.animationScope,
                    fieldState.offsetX,
                    fieldState.context,
                    fieldState.maxSwipe,
                    fieldState.currentOnSwipeToClear
                )
            },
            copyAction = copyAction
        )
    }
}

@Composable
internal fun rememberSwipeToClearState(
    value: String,
    onValueChange: (String) -> Unit,
    onSwipeToClear: () -> Unit,
    isActive: Boolean,
    highlightPulse: Long
): SwipeToClearState {
    val offsetX = remember { Animatable(0f) }
    val hapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current
    val currentValue by rememberUpdatedState(value)
    val currentOnSwipeToClear by rememberUpdatedState(onSwipeToClear)
    val animationScope = rememberCoroutineScope()
    val textFieldState = rememberTextFieldState(value)
    val lastExternalValue = remember { mutableStateOf(value) }
    val density = androidx.compose.ui.platform.LocalDensity.current
    val maxSwipe = with(density) { 80.dp.toPx() }
    val swipeThreshold = with(density) { 56.dp.toPx() }
    val rainbowProgress = remember { Animatable(0f) }
    val activeBorderAlpha by animateFloatAsState(
        targetValue = if (isActive) 1f else 0f,
        animationSpec = tween(durationMillis = 220),
        label = "activeFieldBorder"
    )
    SyncSwipeField(value, highlightPulse, textFieldState, lastExternalValue, rainbowProgress, onValueChange)
    return SwipeToClearState(
        offsetX,
        hapticFeedback,
        context,
        currentValue,
        currentOnSwipeToClear,
        animationScope,
        textFieldState,
        maxSwipe,
        swipeThreshold,
        rainbowProgress,
        activeBorderAlpha,
        RoundedCornerShape(20.dp),
        MaterialTheme.colorScheme.surfaceContainerHighest,
        MaterialTheme.colorScheme.primary
    )
}

internal fun animateClearFromButton(
    currentValue: String,
    hapticFeedback: HapticFeedback,
    animationScope: CoroutineScope,
    offsetX: Animatable<Float, AnimationVector1D>,
    context: Context,
    maxSwipe: Float,
    onSwipeToClear: () -> Unit
) {
    if (currentValue.isEmpty()) return
    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
    animationScope.launch {
        offsetX.stop()
        playClearSound(context)
        offsetX.animateTo(-maxSwipe, tween(durationMillis = 500))
        onSwipeToClear()
        offsetX.animateTo(0f, tween(durationMillis = 500))
    }
}

@Composable
internal fun SyncSwipeField(
    value: String,
    highlightPulse: Long,
    textFieldState: androidx.compose.foundation.text.input.TextFieldState,
    lastExternalValue: androidx.compose.runtime.MutableState<String>,
    rainbowProgress: Animatable<Float, AnimationVector1D>,
    onValueChange: (String) -> Unit
) {
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
        lastExternalValue.value = value
        if (textFieldState.text.toString() != value) {
            textFieldState.setTextAndPlaceCursorAtEnd(value)
        }
    }
    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text.toString() }.collectLatest { newValue ->
            if (newValue != lastExternalValue.value) {
                lastExternalValue.value = newValue
                onValueChange(newValue)
            }
        }
    }
}

internal fun Modifier.clearSwipeGestures(
    offsetX: Animatable<Float, AnimationVector1D>,
    maxSwipe: Float,
    swipeThreshold: Float,
    currentValue: String,
    currentOnSwipeToClear: () -> Unit,
    context: Context,
    hapticFeedback: HapticFeedback,
    animationScope: CoroutineScope
): Modifier = pointerInput(Unit) {
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
                playClearSound(context)
                currentOnSwipeToClear()
            }
            animationScope.launch { offsetX.animateTo(0f, tween(durationMillis = 220)) }
            thresholdReached = false
        },
        onDragCancel = {
            animationScope.launch { offsetX.animateTo(0f, tween(durationMillis = 160)) }
            thresholdReached = false
        }
    )
}

internal fun Modifier.drawSwipeBorder(
    activeBorderAlpha: Float,
    activeBorderColor: Color,
    rainbowProgress: Float,
    highlightPulse: Long
): Modifier = drawWithCache {
    val strokeWidth = 1.dp.toPx()
    val inset = strokeWidth / 2f
    val cornerRadius = 22.dp.toPx()
    onDrawWithContent {
        drawContent()
        if (activeBorderAlpha > 0f) {
            drawRoundRect(
                color = activeBorderColor,
                topLeft = Offset(inset, inset),
                size = Size(size.width - strokeWidth, size.height - strokeWidth),
                cornerRadius = CornerRadius(cornerRadius - inset),
                style = Stroke(width = strokeWidth),
                alpha = activeBorderAlpha
            )
        }
        val alpha = 1f - rainbowProgress
        if (highlightPulse != 0L && alpha > 0f) {
            val colorPhase = rainbowProgress * 360f
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

@Composable
internal fun SwipeToClearFieldContent(
    textFieldState: androidx.compose.foundation.text.input.TextFieldState,
    onFocused: () -> Unit,
    minLines: Int,
    textStyle: TextStyle,
    label: String,
    onClear: () -> Unit,
    copyAction: @Composable () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Box(Modifier.fillMaxWidth()) {
            CompositionLocalProvider(
                LocalAutofillHighlightBrush provides SolidColor(Color.Transparent),
            ) {
                BasicTextField(
                    state = textFieldState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { if (it.isFocused) onFocused() }
                        .padding(start = 16.dp, top = 34.dp, end = 16.dp, bottom = 14.dp),
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
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ClearButton(onClick = onClear)
            copyAction()
        }
    }
}

internal fun playClearSound(context: Context) {
    MediaPlayer.create(context, R.raw.clear_swipe)?.apply {
        setVolume(ClearSoundVolume, ClearSoundVolume)
        setOnCompletionListener { player -> player.release() }
        setOnErrorListener { player, _, _ ->
            player.release()
            true
        }
        start()
    }
}
