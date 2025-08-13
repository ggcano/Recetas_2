package com.example.recetas_2.usecase

import com.example.recetas_2.data.MealRepository
import com.example.recetas_2.data.model.Meal
import com.example.recetas_2.data.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchMealsUseCase @Inject constructor(
    private val repository: MealRepository
) {
    suspend operator fun invoke(query: String): Result<List<Meal>> {
        return try {
            if (query.isBlank()) {
                Result.Error(Exception("Ingresa un término de búsqueda"))
            } else {
                val results = repository.searchMeals(query)
                if (results.isEmpty()) {
                    Result.Error(Exception("No hay resultados para '$query'"))
                } else {
                    Result.Success(results)
                }
            }
        } catch (e: Exception) {
            Result.Error(Exception("Error de conexión: ${e.message}"))
        }
    }
}