package com.movieshelf.app

import android.app.Application
import com.movieshelf.app.di.AppContainer

/**
 * Punkt startowy procesu aplikacji.
 * Trzyma [AppContainer] — jedno miejsce na Room, repozytoria i NetworkMonitor.
 */
class MovieShelfApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
