package com.movieshelf.app.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Obserwuje stan połączenia sieciowego urządzenia.
 * Używane do pokazania ekranu „Brak połączenia” zamiast ogólnego błędu.
 */
class NetworkMonitor(context: Context) {

    private val connectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /** Jednorazowe sprawdzenie: czy jest sieć z dostępem do internetu */
    fun isOnline(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val caps = connectivityManager.getNetworkCapabilities(network) ?: return false
        // Wystarczy capability INTERNET — VALIDATED bywa niestabilne na emulatorze
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /**
     * Strumień true/false — reaguje na włączenie/wyłączenie Wi‑Fi lub danych.
     * distinctUntilChanged: emitujemy tylko gdy stan faktycznie się zmienia.
     */
    fun connectivityFlow(): Flow<Boolean> = callbackFlow {
        // Od razu wysyłamy aktualny stan
        trySend(isOnline())

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(isOnline())
            }

            override fun onLost(network: Network) {
                trySend(isOnline())
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                trySend(isOnline())
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Wyrejestrowanie przy anulowaniu Flow (np. gdy ViewModel jest czyszczony)
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
}
