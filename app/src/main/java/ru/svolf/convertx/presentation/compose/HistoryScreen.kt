package ru.svolf.convertx.presentation.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.svolf.convertx.R
import ru.svolf.convertx.data.HistoryRecord
import java.text.DateFormat
import java.util.Date

@Composable
internal fun HistoryScreen(
    records: List<HistoryRecord>,
    onDelete: (HistoryRecord) -> Unit,
    onOpen: (HistoryRecord) -> Unit
) {
    if (records.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                stringResource(R.string.message_history_empty),
                textAlign = TextAlign.Center
            )
        }
        return
    }
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(records, key = { it.id }) { record ->
            HistorySwipeItem(
                record = record,
                onDelete = { onDelete(record) },
                onOpen = { onOpen(record) }
            )
        }
    }
}

@Composable
private fun HistorySwipeItem(
    record: HistoryRecord,
    onDelete: () -> Unit,
    onOpen: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val maxSwipeDistance = 64.dp
    val swipeOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val hapticFeedback = LocalHapticFeedback.current
    val currentOnDelete by rememberUpdatedState(onDelete)
    val currentOnOpen by rememberUpdatedState(onOpen)
    var thresholdReached = false

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(MaterialTheme.colorScheme.errorContainer),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer { translationX = swipeOffset.value }
                .pointerInput(maxSwipeDistance) {
                    val maxSwipePx = maxSwipeDistance.toPx()
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, dragAmount ->
                            val nextOffset = (swipeOffset.value + dragAmount)
                                .coerceIn(-maxSwipePx, 0f)
                            if (nextOffset != swipeOffset.value) {
                                change.consume()
                                scope.launch { swipeOffset.snapTo(nextOffset) }
                                val isThresholdReached = nextOffset <= -maxSwipePx
                                if (isThresholdReached && !thresholdReached) {
                                    thresholdReached = true
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                } else if (!isThresholdReached) {
                                    thresholdReached = false
                                }
                            }
                        },
                        onDragEnd = {
                            scope.launch {
                                if (swipeOffset.value <= -maxSwipePx) {
                                    currentOnDelete()
                                } else {
                                    swipeOffset.animateTo(0f, tween(durationMillis = 180))
                                }
                            }
                            thresholdReached = false
                        },
                        onDragCancel = {
                            scope.launch {
                                swipeOffset.animateTo(0f, tween(durationMillis = 180))
                            }
                            thresholdReached = false
                        }
                    )
                }
                .clickable { currentOnOpen() },
            shape = shape
        ) {
            Column(Modifier.padding(12.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        record.input,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        decoderName(record.decoder),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Text(record.output, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(
                    DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                        .format(Date(record.id)),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

private fun decoderName(decoder: Int): String = when (decoder) {
    0 -> "Unicode"
    1 -> "Base64"
    2 -> "HEX"
    else -> "Unknown"
}
