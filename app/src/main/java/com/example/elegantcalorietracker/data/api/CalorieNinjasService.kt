package com.example.elegantcalorietracker.data.api

import com.example.elegantcalorietracker.BuildConfig
import com.example.elegantcalorietracker.data.api.RemoteDataSource.httpClient
import com.example.elegantcalorietracker.data.model.FoodList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Private API key from https://api.calorieninjas.com
 */
private const val API_KEY = BuildConfig.API_KEY
private const val BASE_URL = "https://api.calorieninjas.com/v1/"

// Moshi converter
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Retrofit service
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

/**
 * Calls the CalorieNinjas API with the private [API_KEY], returns a [FoodList]
 * object
 */
interface CalorieNinjasService {
    @Headers(
        "X-Api-Key: $API_KEY"
    )
    @GET("nutrition?query")
    suspend fun getFoodList(@Query("query") query: String): FoodList
}

/**
 * Singleton object that implements [CalorieNinjasService] through [httpClient]
 */
object RemoteDataSource {
    /**
     * Lazy declaration of [CalorieNinjasService] implementation by [retrofit]
     */
    val httpClient: CalorieNinjasService by lazy {
        retrofit.create(CalorieNinjasService::class.java)
    }
}
