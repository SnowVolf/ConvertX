package ru.svolf.convertx.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import ru.svolf.convertx.R

@Composable
internal fun AboutScreen(onChangelog: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(R.string.about_preambula))
        Button(onClick = onChangelog) { Text(stringResource(R.string.changelog)) }
        TextButton(onClick = { uriHandler.openUri("https://github.com/SnowVolf/ConvertX/") }) {
            Text(stringResource(R.string.git_hub))
        }
        TextButton(onClick = { uriHandler.openUri("mailto:svolf15@yandex.ru?subject=ConvertX") }) {
            Text("Email")
        }
    }
}

@Composable
internal fun ChangelogScreen() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        text = context.assets.open("CHANGELOG.txt").bufferedReader().use { it.readText() }
    }
    LazyColumn {
        item { Text(text, modifier = Modifier.padding(16.dp), fontFamily = FontFamily.Monospace) }
    }
}
