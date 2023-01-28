package ru.svolf.convertx.di

import dagger.Component
import ru.svolf.convertx.data.DbManager
import ru.svolf.convertx.presentation.screens.history.HistoryFragment

/*
 * Created by SVolf on 26.01.2023, 15:09
 * This file is a part of "ConvertX" project
 */
@Component(modules = [DatabaseModule::class])
interface MainComponent {
    fun injectInHistory(historyFragment: HistoryFragment)
    fun getDbManager(): DbManager
}