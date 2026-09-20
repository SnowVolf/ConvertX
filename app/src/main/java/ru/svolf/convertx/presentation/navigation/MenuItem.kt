package ru.svolf.convertx.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import ru.svolf.convertx.R

internal data class MenuItem(
    val title: Int,
    val icon: ImageVector,
    val route: AppRoute?
)

internal val menuItems = listOf(
    MenuItem(R.string.dr_unicode, Icons.Default.Code, UnicodeRoute),
    MenuItem(R.string.dr_base64, Icons.Default.GridView, Base64Route()),
    MenuItem(R.string.dr_hex, Icons.Default.Storage, HexRoute()),
    MenuItem(R.string.dr_regex_dragon, Icons.Default.Code, RegexRoute),
    MenuItem(R.string.dr_hex_palette, Icons.Default.Palette, PaletteRoute),
    MenuItem(R.string.dr_other1, Icons.Default.Tune, OtherToolsRoute),
    MenuItem(R.string.history, Icons.Default.History, HistoryRoute),
    MenuItem(R.string.settings, Icons.Default.Settings, SettingsRoute),
    MenuItem(R.string.dr_about, Icons.Default.Info, AboutRoute),
    MenuItem(R.string.dr_close_app, Icons.Default.ArrowBack, null)
)

@Composable
internal fun routeTitle(route: AppRoute): String = when (route) {
    UnicodeRoute -> stringResource(R.string.dr_unicode)
    is Base64Route -> stringResource(R.string.dr_base64)
    is HexRoute -> stringResource(R.string.dr_hex)
    is RegexRoute -> stringResource(R.string.dr_regex_dragon)
    is PaletteRoute -> stringResource(R.string.dr_hex_palette)
    is OtherToolsRoute -> stringResource(R.string.dr_other1)
    is HistoryRoute -> stringResource(R.string.history)
    is SettingsRoute -> stringResource(R.string.settings)
    is AboutRoute -> stringResource(R.string.dr_about)
    is ChangelogRoute -> stringResource(R.string.changelog)
    is TextToolRoute -> textToolTitle(route.tool)
}

@Composable
private fun textToolTitle(tool: TextTool): String = when (tool) {
    TextTool.ADLER32 -> stringResource(R.string.checksum_adler32)
    TextTool.CRC32 -> stringResource(R.string.checksum_crc)
    TextTool.XML -> stringResource(R.string.unescape_xml)
    TextTool.TIMESTAMP -> stringResource(R.string.timestamp_converter)
}
