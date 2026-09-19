package ru.svolf.convertx.presentation.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.svolf.convertx.App
import ru.svolf.convertx.presentation.compose.ConvertXApp
import ru.svolf.convertx.presentation.ui.ConvertXTheme
import ru.svolf.convertx.presentation.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val app = LocalContext.current.applicationContext as App
            val settingsViewModel: SettingsViewModel =
                viewModel(factory = app.appComponent.settingsViewModelFactory())
            val settings by settingsViewModel.state.collectAsState()
            ConvertXTheme(settings = settings) {
                ConvertXApp(app.appComponent)
            }
        }
    }
}
