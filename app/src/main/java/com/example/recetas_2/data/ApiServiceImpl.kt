package com.example.recetas_2.data

import retrofit2.Response
import javax.inject.Inject

class ApiServiceImpl @Inject constructor(val apiService: MealApiService) {

    suspend fun searchMeals(query: String): Response<MealResponse> = apiService.searchMeals(query)
    suspend fun detailsMeals(query: String): Response<MealResponse> = apiService.detailsMeals(query)

}