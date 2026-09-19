package ru.svolf.convertx.presentation.compose

import androidx.compose.runtime.Composable
import ru.svolf.convertx.di.AppComponent
import ru.svolf.convertx.presentation.navigation.ConvertXNavigation

@Composable
fun ConvertXApp(component: AppComponent) {
    ConvertXNavigation(component)
}
