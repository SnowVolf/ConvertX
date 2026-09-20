package ru.svolf.convertx.presentation.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ru.svolf.convertx.R
import ru.svolf.convertx.di.AppComponent
import ru.svolf.convertx.presentation.compose.AboutScreen
import ru.svolf.convertx.presentation.compose.ChangelogScreen
import ru.svolf.convertx.presentation.compose.ConverterScreen
import ru.svolf.convertx.presentation.compose.HistoryScreen
import ru.svolf.convertx.presentation.compose.OtherToolsScreen
import ru.svolf.convertx.presentation.compose.PaletteScreen
import ru.svolf.convertx.presentation.compose.RegexScreen
import ru.svolf.convertx.presentation.compose.SettingsScreen
import ru.svolf.convertx.presentation.compose.TextToolScreen
import ru.svolf.convertx.presentation.viewmodel.ConverterViewModel
import ru.svolf.convertx.presentation.viewmodel.HistoryViewModel
import ru.svolf.convertx.presentation.viewmodel.PaletteViewModel
import ru.svolf.convertx.presentation.viewmodel.RegexViewModel
import ru.svolf.convertx.presentation.viewmodel.SettingsViewModel
import ru.svolf.convertx.presentation.viewmodel.TextToolViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConvertXNavigation(component: AppComponent) {
    val backStack = rememberNavBackStack(UnicodeRoute)
    val scope = rememberCoroutineScope()
    val activity = LocalActivity.current
    val snackbar = remember { SnackbarHostState() }
    var exitArmed by rememberSaveable { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    var backdropOpen by rememberSaveable { mutableStateOf(false) }
    val currentRoute = (backStack.lastOrNull() as? AppRoute) ?: UnicodeRoute
    val settingsViewModel: SettingsViewModel =
        viewModel(factory = component.settingsViewModelFactory())
    val settings by settingsViewModel.state.collectAsStateWithLifecycle()
    val pressAgainMessage = stringResource(R.string.press_back_once_more)
    var toolbarActions by remember {
        mutableStateOf<@Composable androidx.compose.foundation.layout.RowScope.() -> Unit>({})
    }

    NavigationBackHandler(
        backdropOpen = backdropOpen,
        onCloseBackdrop = { backdropOpen = false },
        backStack = backStack,
        twiceBackToExit = settings.twiceBackToExit,
        exitArmed = exitArmed,
        onArmExit = { exitArmed = true },
        onDisarmExit = { exitArmed = false },
        snackbar = snackbar,
        pressAgainMessage = pressAgainMessage,
        activity = activity,
        scope = scope
    )
    if (showExitDialog) ExitDialog(activity, onDismiss = { showExitDialog = false })

    Backdrop(
        isOpen = backdropOpen,
        onClose = { backdropOpen = false },
        toolbarContent = { 
            NavigationToolbar(currentRoute, backdropOpen, toolbarActions) { backdropOpen = !backdropOpen } 
        },
        backContent = {
            BackdropContent { route ->
                selectRoute(route, backStack, { backdropOpen = false }, { showExitDialog = true })
            }
        },
        frontContent = { 
            NavigationFront(component, backStack, settings.fontSize, snackbar) { toolbarActions = it } 
        }
    )
}

@Composable
private fun NavigationFront(
    component: AppComponent,
    backStack: MutableList<NavKey>,
    fontSize: Int,
    snackbar: SnackbarHostState,
    setToolbarActions: (@Composable androidx.compose.foundation.layout.RowScope.() -> Unit) -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbar, Modifier.navigationBarsPadding()) }
    ) { padding ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.fillMaxSize().padding(padding),
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<UnicodeRoute> { converterEntry(component, UnicodeRoute, fontSize, snackbar) }
                entry<Base64Route> { converterEntry(component, it, fontSize, snackbar) }
                entry<HexRoute> { converterEntry(component, it, fontSize, snackbar) }
                entry<TextToolRoute> { textToolEntry(component, it, fontSize, snackbar, setToolbarActions) }
                entry<RegexRoute> { regexEntry(component) }
                entry<PaletteRoute> { paletteEntry(component, snackbar) }
                entry<HistoryRoute> { historyEntry(component, backStack) }
                entry<SettingsRoute> { settingsEntry(component) }
                entry<OtherToolsRoute> { OtherToolsScreen { backStack.add(it) } }
                entry<AboutRoute> { AboutScreen(onChangelog = { backStack.add(ChangelogRoute) }) }
                entry<ChangelogRoute> { ChangelogScreen() }
            }
        )
    }
}

@Composable
private fun converterEntry(
    component: AppComponent,
    route: AppRoute,
    fontSize: Int,
    snackbar: SnackbarHostState
) {
    val viewModel: ConverterViewModel =
        viewModel(factory = component.converterViewModelFactory().forRoute(route))
    val state by viewModel.state.collectAsStateWithLifecycle()
    ConverterScreen(route, state, fontSize, viewModel, snackbar)
}

@Composable
private fun textToolEntry(
    component: AppComponent,
    route: TextToolRoute,
    fontSize: Int,
    snackbar: SnackbarHostState,
    setToolbarActions: (@Composable androidx.compose.foundation.layout.RowScope.() -> Unit) -> Unit
) {
    val viewModel: TextToolViewModel =
        viewModel(factory = component.textToolViewModelFactory().forRoute(route))
    val state by viewModel.state.collectAsStateWithLifecycle()
    TextToolScreen(route, state, fontSize, viewModel, snackbar, setToolbarActions)
}

@Composable
private fun historyEntry(component: AppComponent, backStack: MutableList<NavKey>) {
    val viewModel: HistoryViewModel = viewModel(factory = component.historyViewModelFactory())
    val records by viewModel.records.collectAsStateWithLifecycle()
    HistoryScreen(records, viewModel::delete) { record -> record.toRoute()?.let(backStack::add) }
}

@Composable
private fun paletteEntry(component: AppComponent, snackbar: SnackbarHostState) {
    val viewModel: PaletteViewModel = viewModel(factory = component.paletteViewModelFactory())
    PaletteScreen(viewModel.palettes, snackbar)
}

@Composable
private fun settingsEntry(component: AppComponent) {
    val viewModel: SettingsViewModel = viewModel(factory = component.settingsViewModelFactory())
    val state by viewModel.state.collectAsStateWithLifecycle()
    SettingsScreen(state, viewModel)
}

@Composable
private fun regexEntry(component: AppComponent) {
    val viewModel: RegexViewModel = viewModel(factory = component.regexViewModelFactory())
    val state by viewModel.state.collectAsStateWithLifecycle()
    RegexScreen(state, viewModel)
}
