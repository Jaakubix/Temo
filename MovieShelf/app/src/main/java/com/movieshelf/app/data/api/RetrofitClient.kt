package com.movieshelf.app.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Fabryka klienta HTTP / Retrofit.
 * Singleton — jeden OkHttp + Retrofit na całą aplikację (oszczędność pamięci i socketów).
 */
object RetrofitClient {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    /** Loguje URL i kody odpowiedzi w Logcat (bez body — mniej szumu i bezpieczniej) */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /** Gotowa implementacja interfejsu [TmdbApiService] generowana przez Retrofit */
    val tmdbApi: TmdbApiService by lazy {
        retrofit.create(TmdbApiService::class.java)
    }
}
