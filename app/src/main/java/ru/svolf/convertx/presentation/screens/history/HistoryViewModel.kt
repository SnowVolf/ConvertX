package ru.svolf.convertx.presentation.screens.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.svolf.convertx.App
import ru.svolf.convertx.data.entity.HistoryItem

/*
 * Created by SVolf on 26.01.2023, 16:55
 * This file is a part of "ConvertX" project
 */
class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val database = (application as App).mainComponent.getDbManager().getDatabase()
    private val dao = database.historyDao()

    val data = dao.getAll()

    fun remove(itemId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(dao.getById(itemId))
        }
    }


    fun getItem(itemId: Long): HistoryItem {
        val historyItem: HistoryItem = runBlocking {
            withContext(Dispatchers.IO) {
                val historyItem = dao.getById(itemId)
                historyItem
            }
        }
        return historyItem
    }

    override fun onCleared() {
        if (database.isOpen and !database.inTransaction()) {
            database.close()
        }
        super.onCleared()
    }


}