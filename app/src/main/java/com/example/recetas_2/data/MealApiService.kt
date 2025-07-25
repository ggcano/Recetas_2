package com.example.recetas_2.data


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {
    @GET("search.php")
    suspend fun searchMeals(
        @Query("s") query: String
    ): Response<MealResponse>

    @GET("lookup.php")
    suspend fun detailsMeals(
        @Query("i") query: String
    ): Response<MealResponse>


    companion object {
        const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"
    }
}