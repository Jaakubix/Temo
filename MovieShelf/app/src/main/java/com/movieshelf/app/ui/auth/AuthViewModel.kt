package com.movieshelf.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieshelf.app.data.repository.AuthRepository
import com.movieshelf.app.network.NetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class AuthUiState {
    data object Idle : AuthUiState()
    data object Loading : AuthUiState()
    data object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    /** Tryb gościa — półka lokalna bez Firebase (gdy brak google-services) */
    private val _guestMode = MutableStateFlow(false)

    private val firebaseLoggedIn = authRepository.authState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), authRepository.isLoggedIn)

    /** Zalogowany przez Firebase LUB tryb gościa */
    val isLoggedIn: StateFlow<Boolean> = combine(firebaseLoggedIn, _guestMode) { firebase, guest ->
        firebase || guest
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        authRepository.isLoggedIn
    )

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val userEmail: String?
        get() = when {
            _guestMode.value -> "Gość (offline)"
            else -> authRepository.currentUser?.email
        }

    fun signIn(email: String, password: String) {
        submit(email, password, register = false)
    }

    fun register(email: String, password: String) {
        submit(email, password, register = true)
    }

    /** Wejście bez konta — Room i półka działają lokalnie */
    fun continueAsGuest() {
        _guestMode.value = true
        _uiState.value = AuthUiState.Success
    }

    fun signOut() {
        _guestMode.value = false
        authRepository.signOut()
        _uiState.value = AuthUiState.Idle
    }

    fun clearError() {
        if (_uiState.value is AuthUiState.Error) {
            _uiState.value = AuthUiState.Idle
        }
    }

    private fun submit(email: String, password: String, register: Boolean) {
        if (email.isBlank() || password.length < 6) {
            _uiState.value = AuthUiState.Error("Podaj e-mail i hasło (min. 6 znaków).")
            return
        }
        if (!networkMonitor.isOnline()) {
            _uiState.value = AuthUiState.Error("Brak połączenia — zaloguj się online albo wejdź jako gość.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = if (register) {
                authRepository.register(email, password)
            } else {
                authRepository.signIn(email, password)
            }
            _uiState.value = result.fold(
                onSuccess = {
                    _guestMode.value = false
                    AuthUiState.Success
                },
                onFailure = { AuthUiState.Error(mapAuthError(it)) }
            )
        }
    }

    private fun mapAuthError(t: Throwable): String {
        val raw = t.message.orEmpty()
        return when {
            raw.contains("password is invalid", true) ||
                raw.contains("INVALID_LOGIN", true) ||
                raw.contains("invalid-credential", true) ->
                "Nieprawidłowy e-mail lub hasło."
            raw.contains("email address is already", true) ||
                raw.contains("email-already-in-use", true) ->
                "Ten e-mail jest już zarejestrowany."
            raw.contains("badly formatted", true) ||
                raw.contains("invalid-email", true) ->
                "Niepoprawny format e-mail."
            raw.contains("network", true) ->
                "Problem z siecią. Spróbuj ponownie."
            raw.contains("API key", true) ||
                raw.contains("CONFIGURATION", true) ||
                raw.contains("deactivated", true) ->
                "Firebase niegotowy — podmień google-services.json albo wejdź jako gość."
            else -> raw.ifBlank { "Nie udało się zalogować." }
        }
    }
}
