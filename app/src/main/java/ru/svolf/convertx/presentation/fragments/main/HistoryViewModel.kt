package ru.svolf.convertx.presentation.fragments.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.svolf.convertx.App
import ru.svolf.convertx.data.entity.HistoryItem

/*
 * Created by SVolf on 26.01.2023, 16:55
 * This file is a part of "ConvertX" project
 */
class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private var dao = (application as App).mainComponent.getDbManager().getDatabase().historyDao()

    val data = dao.getAll()

    fun remove(item: HistoryItem) {
        dao.delete(item)
    }
}