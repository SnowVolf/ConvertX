package ru.svolf.convertx.presentation.ui

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.WindowCompat
import ru.svolf.convertx.R
import ru.svolf.convertx.data.SettingsState
import ru.svolf.convertx.data.ThemeMode

@Composable
fun ConvertXTheme(settings: SettingsState, content: @Composable () -> Unit) {
    val dark = settings.theme == ThemeMode.DARK
    val context = LocalContext.current
    val colors = if (dark) {
        val base = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            dynamicDarkColorScheme(context)
        } else {
            darkColorScheme(
                primary = colorResource(R.color.md_theme_dark_primary),
                onPrimary = colorResource(R.color.md_theme_dark_onPrimary),
                primaryContainer = colorResource(R.color.md_theme_dark_primaryContainer),
                onPrimaryContainer = colorResource(R.color.md_theme_dark_onPrimaryContainer),
                background = colorResource(R.color.md_theme_dark_background),
                onBackground = colorResource(R.color.md_theme_dark_onBackground),
                surface = colorResource(R.color.md_theme_dark_surface),
                onSurface = colorResource(R.color.md_theme_dark_onSurface)
            )
        }
        base.copy(
            background = Color(0xFF0A2023),
            surface = Color(0xFF14282A),
            surfaceContainerLowest = Color(0xFF071719),
            surfaceContainerLow = Color(0xFF0D2022),
            surfaceContainer = Color(0xFF14282A),
            surfaceContainerHigh = Color(0xFF25282C),
            surfaceContainerHighest = Color(0xFF302E35),
            onBackground = Color(0xFFE5F0F1),
            onSurface = Color(0xFFE5F0F1)
        )
    } else {
        val base = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            dynamicLightColorScheme(context)
        } else {
            lightColorScheme(
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
        base.copy(
            background = Color(0xFFF1F3F4),
            surface = Color.White,
            surfaceContainerLowest = Color.White,
            surfaceContainerLow = Color.White,
            surfaceContainer = Color.White,
            surfaceContainerHigh = Color.White,
            surfaceContainerHighest = Color.White,
            onBackground = Color(0xFF1B1B1F),
            onSurface = Color(0xFF1B1B1F)
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !dark
            controller.isAppearanceLightNavigationBars = !dark
        }
    }

    MaterialTheme(colorScheme = colors, typography = typography, content = content)
}
