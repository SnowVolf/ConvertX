package ru.svolf.convertx.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.svolf.convertx.R
import ru.svolf.convertx.presentation.navigation.TextTool
import ru.svolf.convertx.presentation.navigation.TextToolRoute

@Composable
internal fun OtherToolsScreen(onOpen: (TextToolRoute) -> Unit) {
    val tools = listOf(
        TextTool.ADLER32 to R.string.checksum_adler32,
        TextTool.CRC32 to R.string.checksum_crc,
        TextTool.XML to R.string.unescape_xml,
        TextTool.TIMESTAMP to R.string.timestamp_converter
    )
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(tools) { (tool, title) ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpen(TextToolRoute(tool)) }) {
                Text(
                    stringResource(title),
                    modifier = Modifier.padding(18.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
