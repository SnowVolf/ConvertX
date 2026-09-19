package ru.svolf.convertx.presentation.navigation

import ru.svolf.convertx.data.HistoryRecord

internal fun HistoryRecord.toRoute(): AppRoute? = when (decoder) {
    0 -> UnicodeRoute
    1 -> Base64Route(input, output, spinnerPosition)
    2 -> HexRoute(input, output, spinnerPosition)
    else -> null
}
