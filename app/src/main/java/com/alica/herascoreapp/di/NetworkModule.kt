package com.alica.herascoreapp.di

import com.alica.herascoreapp.data.MatchRepository
import com.alica.herascoreapp.data.remote.TheSportsDbApi
import com.squareup.moshi.Moshi
import com.alica.herascoreapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {
    private const val BASE_URL = "https://www.thesportsdb.com/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient)
            .build()
    }

    val api: TheSportsDbApi by lazy { retrofit.create(TheSportsDbApi::class.java) }

    // Use documented demo key "3" if no key is configured
    //chapter 3 added
    private val apiKey: String by lazy { BuildConfig.THESPORTSDB_API_KEY }

    val repository: MatchRepository by lazy { MatchRepository(api, apiKey) }
}


