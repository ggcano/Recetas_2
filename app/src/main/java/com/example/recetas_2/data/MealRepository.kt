package com.example.recetas_2.data

import com.example.recetas_2.data.model.Meal
import javax.inject.Inject


class MealRepository
@Inject
constructor(val api: ApiServiceImpl) {


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
