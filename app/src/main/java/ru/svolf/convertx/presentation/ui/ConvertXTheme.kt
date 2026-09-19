package ru.svolf.convertx.presentation.ui

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import ru.svolf.convertx.R
import ru.svolf.convertx.data.SettingsState
import ru.svolf.convertx.data.ThemeMode

@Composable
fun ConvertXTheme(settings: SettingsState, content: @Composable () -> Unit) {
    val dark = settings.theme == ThemeMode.DARK
    val context = LocalContext.current
    val colors = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && dark -> dynamicDarkColorScheme(context)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> dynamicLightColorScheme(context)
        dark -> darkColorScheme(
            primary = colorResource(R.color.md_theme_dark_primary),
            onPrimary = colorResource(R.color.md_theme_dark_onPrimary),
            primaryContainer = colorResource(R.color.md_theme_dark_primaryContainer),
            onPrimaryContainer = colorResource(R.color.md_theme_dark_onPrimaryContainer),
            background = colorResource(R.color.md_theme_dark_background),
            onBackground = colorResource(R.color.md_theme_dark_onBackground),
            surface = colorResource(R.color.md_theme_dark_surface),
            onSurface = colorResource(R.color.md_theme_dark_onSurface)
        )

        else -> lightColorScheme(
            primary = colorResource(R.color.md_theme_light_primary),
            onPrimary = colorResource(R.color.md_theme_light_onPrimary),
            primaryContainer = colorResource(R.color.md_theme_light_primaryContainer),
            onPrimaryContainer = colorResource(R.color.md_theme_light_onPrimaryContainer),
            background = colorResource(R.color.md_theme_light_background),
            onBackground = colorResource(R.color.md_theme_light_onBackground),
            surface = colorResource(R.color.md_theme_light_surface),
            onSurface = colorResource(R.color.md_theme_light_onSurface)
        )
    }
    val googleSans = FontFamily(
        Font(R.font.googlesans_regular, FontWeight.Normal),
        Font(R.font.googlesans_medium, FontWeight.Medium),
        Font(R.font.googlesans_bold, FontWeight.Bold)
    )
    val typography = Typography().let { base ->
        base.copy(
            bodyLarge = base.bodyLarge.copy(fontFamily = googleSans),
            bodyMedium = base.bodyMedium.copy(fontFamily = googleSans),
            titleLarge = base.titleLarge.copy(fontFamily = googleSans),
            headlineSmall = base.headlineSmall.copy(fontFamily = googleSans)
        )
    }
    MaterialTheme(colorScheme = colors, typography = typography, content = content)
}
