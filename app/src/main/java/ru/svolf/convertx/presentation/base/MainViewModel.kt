package ru.svolf.convertx.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.svolf.convertx.R
import ru.svolf.convertx.data.model.MainMenuVH

class MainViewModel : ViewModel() {
    private val list = MutableLiveData<List<MainMenuVH>>()

    private fun getItems() : List<MainMenuVH> {
        return listOf(
            MainMenuVH(R.id.unicodeFragment, R.drawable.ic_dr_unicode, R.string.dr_unicode),
            MainMenuVH(R.id.base64Fragment, R.drawable.ic_dr_base64, R.string.dr_base64),
            MainMenuVH(R.id.hexFragment, R.drawable.ic_dr_hex, R.string.dr_hex),
            MainMenuVH(R.id.regexDragonFragment, R.drawable.ic_dr_regexdragon, R.string.dr_regex_dragon),
            MainMenuVH(R.id.paletteFragment, R.drawable.ic_dr_palette, R.string.dr_hex_palette),
            MainMenuVH(R.id.listCoders, R.drawable.ic_dr_other_coders, R.string.dr_other1),
            MainMenuVH(R.id.historyFragment, R.drawable.ic_history, R.string.history),
            MainMenuVH(R.id.settingsFragment, R.drawable.ic_dr_settings, R.string.settings),
            MainMenuVH(R.id.aboutFragment, R.drawable.ic_info, R.string.dr_about),
            MainMenuVH(android.R.id.home, R.drawable.ic_action_arrow_back, R.string.dr_close_app)
        )
    }

    fun getData(): LiveData<List<MainMenuVH>>{
        list.value = getItems()
        return list
    }
}