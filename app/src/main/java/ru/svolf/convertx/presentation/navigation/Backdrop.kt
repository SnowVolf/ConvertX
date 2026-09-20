package ru.svolf.convertx.presentation.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.min

@Composable
internal fun Backdrop(
    isOpen: Boolean,
    onClose: () -> Unit,
    toolbarContent: @Composable () -> Unit,
    backContent: @Composable () -> Unit,
    frontContent: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        toolbarContent()
        BackdropLayers(Modifier.weight(1f), isOpen, onClose, backContent, frontContent)
    }
}

@Composable
private fun BackdropLayers(
    modifier: Modifier,
    isOpen: Boolean,
    onClose: () -> Unit,
    backContent: @Composable () -> Unit,
    frontContent: @Composable () -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .clipToBounds()
    ) {
        var backHeightPx by remember { mutableIntStateOf(0) }
        val density = androidx.compose.ui.platform.LocalDensity.current
        val maxHeightPx = with(density) { maxHeight.toPx() }
        val targetOffset = if (isOpen) {
            with(density) { min(backHeightPx.toFloat(), maxHeightPx).toDp() }
        } else {
            0.dp
        }
        val offset by animateDpAsState(
            targetValue = targetOffset,
            animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
            label = "backdropOffset"
        )
        val frontShape = if (isOpen) {
            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        } else {
            RectangleShape
        }

        BackdropBackLayer(backHeightPx, { backHeightPx = it }, backContent)
        BackdropFrontLayer(offset, frontShape, frontContent)
        if (isOpen) BackdropScrim(offset, onClose)
    }
}

@Composable
private fun BackdropBackLayer(
    backHeightPx: Int,
    onHeightChanged: (Int) -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .onGloballyPositioned { coordinates ->
                if (backHeightPx != coordinates.size.height) onHeightChanged(coordinates.size.height)
            }
            .zIndex(0f)
    ) {
        content()
    }
}

@Composable
private fun BackdropFrontLayer(
    offset: androidx.compose.ui.unit.Dp,
    shape: androidx.compose.ui.graphics.Shape,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = offset)
            .clip(shape)
            .zIndex(1f)
    ) {
        content()
    }
}

@Composable
private fun BackdropScrim(
    offset: androidx.compose.ui.unit.Dp,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = offset)
            .zIndex(2f)
            .clickable(onClick = onClose)
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.20f))
    )
}
