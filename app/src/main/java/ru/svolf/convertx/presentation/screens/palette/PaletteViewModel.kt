package ru.svolf.convertx.presentation.screens.palette

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.svolf.convertx.App
import ru.svolf.convertx.data.entity.Palette
import ru.svolf.convertx.data.entity.Squash
import ru.svolf.convertx.data.model.ColorsVH
import ru.svolf.convertx.data.model.PaletteVH

/*
 * Created by SVolf on 23.01.2023, 18:07
 * This file is a part of "ConvertX" project
 */
class PaletteViewModel(application: Application) : AndroidViewModel(application) {
    private val squash: Squash
    private val colorsGallery: MutableLiveData<List<Palette>> = MutableLiveData()
    private val currentPalette: MutableLiveData<Palette>

    init {
        squash = Json.decodeFromString(getColors())
        currentPalette = MutableLiveData(squash.palettes[0])
        colorsGallery.value = squash.palettes
    }

    fun getCurrentPalette() = Transformations.map(currentPalette) { palette ->
        palette.colors.map {
            PaletteVH(it)
        }.toList()
    }

    fun setCurrentPalette(index: Int) {
        currentPalette.value = squash.palettes[index]
    }

    fun getAllPalettes() = Transformations.map(colorsGallery) { list ->
        list.map {
            ColorsVH(it)
        }.toList()
    }

    private fun getColors(): String {
        val inputStream = getApplication<App>().assets.open("colors_palette.json")
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer)
    }
}