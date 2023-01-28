package ru.svolf.convertx.presentation.screens.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.svolf.convertx.App

/*
 * Created by SVolf on 26.01.2023, 16:55
 * This file is a part of "ConvertX" project
 */
class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val database = (application as App).mainComponent.getDbManager().getDatabase()
    private val dao = database.historyDao()

    val data = dao.getAll()

    fun remove(itemId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.delete(dao.getById(itemId))
        }
    }

    override fun onCleared() {
        if (database.isOpen and !database.inTransaction()) {
            database.close()
        }
        super.onCleared()
    }


}