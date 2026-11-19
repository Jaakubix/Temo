package com.movieshelf.app.network

/**
 * Jednolity wynik operacji sieciowej.
 * UI i ViewModel nie muszą łapać wyjątków — dostają jeden z wariantów.
 */
sealed class ApiResult<out T> {

    /** Udane zapytanie z danymi */
    data class Success<T>(val data: T) : ApiResult<T>()

    /**
     * Błąd HTTP / parsowania / timeoutu itd.
     * @param message komunikat czytelny dla użytkownika
     * @param code opcjonalny kod HTTP (np. 401, 404, 500)
     */
    data class Error(
        val message: String,
        val code: Int? = null,
        val cause: Throwable? = null
    ) : ApiResult<Nothing>()

    /** Brak Wi‑Fi / danych mobilnych — sprawdzane PRZED wywołaniem API */
    data object NoConnection : ApiResult<Nothing>()
}
