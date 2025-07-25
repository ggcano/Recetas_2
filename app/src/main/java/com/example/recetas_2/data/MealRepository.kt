package com.example.recetas_2.data

import com.example.recetas_2.data.MealApiService.Companion.BASE_URL
import com.example.recetas_2.model.Meal
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MealRepository {
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MealApiService::class.java)

    suspend fun searchMeals(query: String): List<Meal> {
        val response = api.searchMeals(query)
        return if (response.isSuccessful) {
            response.body()?.meals ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun detailMeals(query: String): List<Meal> {
        val response = api.detailsMeals(query)
        return if (response.isSuccessful) {
            response.body()?.meals ?: emptyList()
        } else {
            emptyList()
        }
    }
}
