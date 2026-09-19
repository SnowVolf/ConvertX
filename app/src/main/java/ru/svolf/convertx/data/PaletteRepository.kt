package ru.svolf.convertx.data

import android.content.Context
import kotlinx.serialization.json.Json
import ru.svolf.convertx.data.entity.Squash

class PaletteRepository(private val context: Context) {
    fun load(): Squash = context.assets.open("colors_palette.json").use { stream ->
        Json.decodeFromString(stream.bufferedReader().readText())
    }
}
