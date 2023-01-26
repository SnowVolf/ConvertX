package ru.svolf.convertx.presentation.fragments.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.svolf.convertx.App
import ru.svolf.convertx.data.entity.Palette
import ru.svolf.convertx.data.entity.Squash
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/*
 * Created by SVolf on 23.01.2023, 18:07
 * This file is a part of "ConvertX" project
 */
class PaletteViewModel(application: Application) : AndroidViewModel(application) {
    private val palettes: MutableLiveData<List<Palette>>

    init {
        val squash = Json.decodeFromString<Squash>(getColors())
        palettes = MutableLiveData(squash.palettes)
    }

    fun getPalettes(): LiveData<List<Palette>> {
        return palettes
    }

    private fun getColors(): String {
        val sb = StringBuilder()
        try {
            val br = BufferedReader(InputStreamReader(getApplication<App>().assets.open("colors_palette.json"), "UTF-8"))
            br.lines().forEach {
                sb.append(it)
            }
            br.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return sb.toString()
    }
}