package ru.svolf.convertx

import android.app.Application
import ru.svolf.convertx.di.AppComponent
import ru.svolf.convertx.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}
