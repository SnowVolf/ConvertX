package ru.svolf.convertx.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.svolf.convertx.R
import ru.svolf.convertx.ui.fragments.base.MainMenuItem

class MainViewModel : ViewModel() {
    private val list = MutableLiveData<List<MainMenuItem>>()

    private fun getItems() : List<MainMenuItem> {
        return listOf(
            MainMenuItem(R.id.unicodeFragment, R.drawable.ic_dr_unicode, R.string.dr_unicode),
            MainMenuItem(R.id.base64Fragment, R.drawable.ic_dr_base64, R.string.dr_base64),
            MainMenuItem(R.id.hexFragment, R.drawable.ic_dr_hex, R.string.dr_hex),
            MainMenuItem(R.id.regexDragonFragment, R.drawable.ic_dr_regexdragon, R.string.dr_regex_dragon),
            MainMenuItem(R.id.paletteActivity, R.drawable.ic_dr_palette, R.string.dr_hex_palette),
            MainMenuItem(R.id.listCoders, R.drawable.ic_dr_other_coders, R.string.dr_other1),
            MainMenuItem(R.id.aboutFragment, R.drawable.ic_info, R.string.dr_about),
            MainMenuItem(R.id.settingsFragment, R.drawable.ic_dr_settings, R.string.settings),
            MainMenuItem(android.R.id.home, R.drawable.ic_action_arrow_back, R.string.dr_close_app)
        )
    }

    fun getData(): LiveData<List<MainMenuItem>>{
        list.value = getItems()
        return list
    }
}