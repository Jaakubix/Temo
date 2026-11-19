package com.movieshelf.app.di

import android.content.Context
import com.movieshelf.app.data.local.MovieDatabase
import com.movieshelf.app.data.repository.AuthRepository
import com.movieshelf.app.data.repository.MovieRepository
import com.movieshelf.app.data.repository.ShelfRepository
import com.movieshelf.app.network.NetworkMonitor

/**
 * Prosty kontener zależności (bez Hilt).
 * Tworzony raz w [com.movieshelf.app.MovieShelfApplication] —
 * wspólne repozytoria = wspólny globalny stan danych.
 */
class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    val networkMonitor: NetworkMonitor by lazy {
        NetworkMonitor(appContext)
    }

    private val database: MovieDatabase by lazy {
        MovieDatabase.getInstance(appContext)
    }

    val shelfRepository: ShelfRepository by lazy {
        ShelfRepository(database.movieDao())
    }

    val movieRepository: MovieRepository by lazy {
        MovieRepository(networkMonitor = networkMonitor)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository()
    }
}
