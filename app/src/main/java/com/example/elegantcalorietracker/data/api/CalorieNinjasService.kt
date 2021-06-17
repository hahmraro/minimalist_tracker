package com.example.elegantcalorietracker.data.api

import com.example.elegantcalorietracker.data.model.FoodList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private const val API_KEY = "ZDa8pkzWCggjSktFI3wT4Q==sojF8YEf4NHiKVoW"
private const val BASE_URL = "https://api.calorieninjas.com/v1/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface CalorieNinjasService {
    @Headers(
        "X-Api-Key: $API_KEY"
    )
    @GET("nutrition?query")
    suspend fun getFoodList(@Query("query") query: String): FoodList
}

object CalorieNinjasApi {
    val retrofitService: CalorieNinjasService by lazy {
        retrofit.create(CalorieNinjasService::class.java)
    }
}
