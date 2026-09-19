package ru.svolf.convertx.presentation.viewmodel

import androidx.lifecycle.ViewModel
import ru.svolf.convertx.data.PaletteRepository
import javax.inject.Inject

class PaletteViewModel @Inject constructor(repository: PaletteRepository) : ViewModel() {
    val palettes = repository.load().palettes.toList()
    var selectedIndex: Int = 0
        private set

    fun select(index: Int) {
        selectedIndex = index.coerceIn(palettes.indices)
    }
}
