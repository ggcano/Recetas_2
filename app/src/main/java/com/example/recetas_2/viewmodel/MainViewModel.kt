package com.example.recetas_2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.recetas_2.data.MealRepository
import com.example.recetas_2.model.Meal
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = MealRepository()
    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>> = _meals

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun searchMeals(query: String) {
        if (query.isBlank()) {
            _error.value = "Ingresa un término de búsqueda"
            return
        }

        viewModelScope.launch {
            try {
               // _isLoading.value = true
                val results = repository.searchMeals(query)
                _meals.value = if (results.isEmpty()) {
                    _error.value = "No hay resultados para '$query'"
                    emptyList()
                } else {
                    _error.value = null
                    results
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
               // _isLoading.value = false
            }
        }
    }

}