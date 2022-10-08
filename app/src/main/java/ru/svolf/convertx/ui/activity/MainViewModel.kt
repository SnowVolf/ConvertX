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
            MainMenuItem(1, R.drawable.ic_dr_unicode, R.string.dr_unicode),
            MainMenuItem(2, R.drawable.ic_dr_base64, R.string.dr_base64),
            MainMenuItem(3, R.drawable.ic_dr_hex, R.string.dr_hex),
            MainMenuItem(4, R.drawable.ic_dr_regexdragon, R.string.dr_regex_dragon),
            MainMenuItem(5, R.drawable.ic_dr_palette, R.string.dr_hex_palette),
            MainMenuItem(6, R.drawable.ic_dr_other_coders, R.string.dr_other1),
            MainMenuItem(7, R.drawable.ic_info, R.string.dr_about),
            MainMenuItem(8, R.drawable.ic_dr_settings, R.string.settings),
            MainMenuItem(9, R.drawable.ic_action_arrow_back, R.string.dr_close_app)
        )
    }

    fun getData(): LiveData<List<MainMenuItem>>{
        list.value = getItems()
        return list
    }
}