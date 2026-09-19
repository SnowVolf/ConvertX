package ru.svolf.convertx.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute : NavKey

@Serializable
data object UnicodeRoute : AppRoute
@Serializable
data class Base64Route(
    val input: String? = null,
    val output: String? = null,
    val mode: Int = 0
) : AppRoute

@Serializable
data class HexRoute(
    val input: String? = null,
    val output: String? = null,
    val mode: Int = 0
) : AppRoute

@Serializable
data object RegexRoute : AppRoute
@Serializable
data object PaletteRoute : AppRoute
@Serializable
data object OtherToolsRoute : AppRoute
@Serializable
data object HistoryRoute : AppRoute
@Serializable
data object SettingsRoute : AppRoute
@Serializable
data object AboutRoute : AppRoute
@Serializable
data object ChangelogRoute : AppRoute
@Serializable
data class TextToolRoute(val tool: TextTool) : AppRoute

@Serializable
enum class TextTool { ADLER32, CRC32, XML, TIMESTAMP }
