package ru.svolf.convertx.presentation.screens.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.svolf.convertx.App
import ru.svolf.convertx.data.entity.HistoryItem
import ru.svolf.convertx.data.model.HistoryVH

/*
 * Created by SVolf on 26.01.2023, 16:55
 * This file is a part of "ConvertX" project
 */
class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val database = (application as App).mainComponent.getDbManager().getDatabase()
    private val dao = database.historyDao()

    val data: LiveData<List<HistoryVH>> = Transformations.map(dao.getAll()) { histories ->
        histories.map(::HistoryVH)
    }

    fun remove(itemId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(dao.getById(itemId))
        }
    }


    fun getItem(itemId: Long): HistoryItem {
        return runBlocking {
			withContext(Dispatchers.IO) {
				val historyItem = dao.getById(itemId)
				historyItem
			}
		}
    }

    override fun onCleared() {
        if (database.isOpen and !database.inTransaction()) {
            database.close()
        }
        super.onCleared()
    }


}