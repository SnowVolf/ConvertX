package ru.svolf.convertx.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
import ru.svolf.convertx.presentation.viewmodel.ConverterViewModel
import ru.svolf.convertx.presentation.viewmodel.HistoryViewModel
import ru.svolf.convertx.presentation.viewmodel.PaletteViewModel
import ru.svolf.convertx.presentation.viewmodel.RegexViewModel
import ru.svolf.convertx.presentation.viewmodel.SettingsViewModel
import kotlin.time.Duration.Companion.seconds

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

    LaunchedEffect(exitArmed) {
        if (exitArmed) {
            delay(2.seconds)
            exitArmed = false
        }
    }

    BackHandler {
        when {
            backdropOpen -> backdropOpen = false
            backStack.size > 1 -> backStack.removeLastOrNull()
            settings.twiceBackToExit && !exitArmed -> {
                exitArmed = true
                scope.launch { snackbar.showSnackbar(pressAgainMessage) }
            }

            else -> activity?.finish()
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            text = { Text(stringResource(R.string.dr_close_app)) },
            confirmButton = {
                TextButton(onClick = { activity?.finish() }) { Text(stringResource(R.string.yes)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showExitDialog = false
                }) { Text(stringResource(R.string.no)) }
            }
        )
    }

    Backdrop(
        isOpen = backdropOpen,
        onClose = { backdropOpen = false },
        toolbarContent = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 8.dp),
                title = {
                    Text(
                        text = routeTitle(currentRoute),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    Card(
                        modifier = Modifier
                            .size(46.dp),
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        )
                    ) {
                        IconButton(onClick = { backdropOpen = !backdropOpen }) {
                            Icon(
                                imageVector = if (backdropOpen) Icons.Default.Close else Icons.Default.Menu,
                                contentDescription = stringResource(
                                    if (backdropOpen) R.string.dr_close_app else R.string.dr_other1
                                )
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        backContent = {
            BackdropContent(
                onRouteSelected = { route ->
                    backdropOpen = false
                    if (route == null) {
                        showExitDialog = true
                    } else {
                        backStack.clear()
                        backStack.add(UnicodeRoute)
                        if (route != UnicodeRoute) backStack.add(route)
                    }
                }
            )
        },
        frontContent = {
            Scaffold(
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                snackbarHost = { SnackbarHost(snackbar, Modifier.navigationBarsPadding()) }
            ) { padding ->
                NavDisplay(
                    backStack = backStack,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    onBack = { backStack.removeLastOrNull() },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    entryProvider = entryProvider {
                        entry<UnicodeRoute> {
                            converterEntry(
                                component,
                                UnicodeRoute,
                                settings.fontSize,
                                snackbar
                            )
                        }
                        entry<Base64Route> { key ->
                            converterEntry(
                                component,
                                key,
                                settings.fontSize,
                                snackbar
                            )
                        }
                        entry<HexRoute> { key ->
                            converterEntry(
                                component,
                                key,
                                settings.fontSize,
                                snackbar
                            )
                        }
                        entry<TextToolRoute> { key ->
                            converterEntry(
                                component,
                                key,
                                settings.fontSize,
                                snackbar
                            )
                        }
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
    )
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
