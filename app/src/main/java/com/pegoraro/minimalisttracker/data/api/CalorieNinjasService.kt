package com.pegoraro.minimalisttracker.data.api

import com.pegoraro.minimalisttracker.BuildConfig
import com.pegoraro.minimalisttracker.data.api.RemoteDataSource.httpClient
import com.pegoraro.minimalisttracker.data.model.FoodList
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
 * Returns a [FoodList] object because the returning json file from the API
 * has only one hey value called *items*, which is a list of foods
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
    val httpClient: CalorieNinjasService by lazy {
        retrofit.create(CalorieNinjasService::class.java)
    }
}
