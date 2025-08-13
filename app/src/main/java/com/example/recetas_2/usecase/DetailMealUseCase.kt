package com.example.recetas_2.usecase

import com.example.recetas_2.data.MealRepository
import com.example.recetas_2.data.model.Meal
import com.example.recetas_2.data.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailMealUseCase @Inject constructor(
    private val repository: MealRepository
) {
    suspend operator fun invoke(query: String): Result<Meal> {
        return try {
            val result = repository.detailMeals(query)
            if (result.isEmpty()) {
                Result.Error(Exception("No se encontraron detalles para la comida"))
            } else {
                Result.Success(result[0])
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}