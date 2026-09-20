package ru.svolf.convertx.presentation.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.svolf.convertx.R
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun NavigationBackHandler(
    backdropOpen: Boolean,
    onCloseBackdrop: () -> Unit,
    backStack: MutableList<NavKey>,
    twiceBackToExit: Boolean,
    exitArmed: Boolean,
    onArmExit: () -> Unit,
    onDisarmExit: () -> Unit,
    snackbar: SnackbarHostState,
    pressAgainMessage: String,
    activity: Activity?,
    scope: CoroutineScope
) {
    LaunchedEffect(exitArmed) {
        if (exitArmed) {
            delay(2.seconds)
            onDisarmExit()
        }
    }
    BackHandler {
        when {
            backdropOpen -> onCloseBackdrop()
            backStack.size > 1 -> backStack.removeLastOrNull()
            twiceBackToExit && !exitArmed -> {
                onArmExit()
                scope.launch { snackbar.showSnackbar(pressAgainMessage) }
            }

            else -> activity?.finish()
        }
    }
}

@Composable
internal fun ExitDialog(activity: Activity?, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = { Text(stringResource(R.string.dr_close_app)) },
        confirmButton = {
            TextButton(onClick = { activity?.finish() }) { Text(stringResource(R.string.yes)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.no)) } }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun NavigationToolbar(
    currentRoute: AppRoute,
    backdropOpen: Boolean,
    actions: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit = {},
    onMenuClick: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.padding(horizontal = 8.dp),
        title = { Text(routeTitle(currentRoute), style = MaterialTheme.typography.titleMedium) },
        navigationIcon = {
            Card(
                modifier = Modifier.size(46.dp),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = if (backdropOpen) Icons.Default.Close else Icons.Default.Menu,
                        contentDescription = stringResource(
                            if (backdropOpen) R.string.dr_close_app else R.string.dr_other1
                        )
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

internal fun selectRoute(
    route: AppRoute?,
    backStack: MutableList<NavKey>,
    closeBackdrop: () -> Unit,
    showExitDialog: () -> Unit
) {
    closeBackdrop()
    if (route == null) {
        showExitDialog()
    } else {
        backStack.clear()
        backStack.add(UnicodeRoute)
        if (route != UnicodeRoute) backStack.add(route)
    }
}
