package ru.svolf.convertx.presentation.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass

@Composable
internal fun useSideBySideConverter(): Boolean =
    currentWindowAdaptiveInfoV2().windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
    )

/** Keeps the same field compositions (including selection and focus) when the window resizes. */
@Composable
internal fun AdaptiveConverterLayout(
    sideBySide: Boolean,
    input: @Composable () -> Unit,
    controls: @Composable () -> Unit,
    output: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    controlsWidth: Dp = 64.dp,
    modeSelector: @Composable () -> Unit = {}
) {
    Layout(
        modifier = modifier.fillMaxWidth(),
        content = {
            Box { input() }
            Box { controls() }
            Box { output() }
            Box { modeSelector() }
        }
    ) { measurables, constraints ->
        val width = constraints.maxWidth
        val gap = controlsWidth.roundToPx().coerceAtMost(width)
        val fieldWidth = if (sideBySide) (width - gap) / 2 else width
        val fieldConstraints = constraints.copy(minWidth = fieldWidth, maxWidth = fieldWidth, minHeight = 0)
        val inputField = measurables[0].measure(fieldConstraints)
        val controlBar = measurables[1].measure(
            constraints.copy(minWidth = 0, maxWidth = if (sideBySide) gap else width, minHeight = 0)
        )
        val outputField = measurables[2].measure(fieldConstraints)
        val modeSelector = measurables[3].measure(constraints.copy(minWidth = 0, minHeight = 0))
        val fieldsHeight = maxOf(inputField.height, outputField.height, controlBar.height)
        val controlsHeight = maxOf(controlBar.height, modeSelector.height)
        val height = if (sideBySide) {
            modeSelector.height + fieldsHeight
        } else {
            inputField.height + controlsHeight + outputField.height
        }
        layout(width, constraints.constrainHeight(height)) {
            if (sideBySide) {
                modeSelector.placeRelative(0, 0)
                inputField.placeRelative(0, modeSelector.height)
                outputField.placeRelative(width - outputField.width, modeSelector.height)
                controlBar.placeRelative(
                    (width - controlBar.width) / 2,
                    modeSelector.height + (fieldsHeight - controlBar.height) / 2
                )
            } else {
                inputField.placeRelative(0, 0)
                modeSelector.placeRelative(0, inputField.height)
                controlBar.placeRelative((width - controlBar.width) / 2, inputField.height)
                outputField.placeRelative(0, inputField.height + controlsHeight)
            }
        }
    }
}
