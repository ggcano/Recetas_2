package com.example.recetas_2.viewmodel

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.recetas_2.data.MealRepository
import com.example.recetas_2.model.Meal
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class MainViewModel : ViewModel() {
    private val repository = MealRepository()
    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>> = _meals

    private val _detailsMeals = MutableLiveData<Meal>()
    val detailMeals: LiveData<Meal> = _detailsMeals

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

    fun detailMealsQuery(query: String){
        viewModelScope.launch {
            try {
                val result = repository.detailMeals(query)
                _detailsMeals.postValue(result[0])
            } catch (e: Exception) {
                Log.e("MealsViewModel", "Error cargando detalles de comidas", e)
                _detailsMeals.postValue(null) // o null según lo que prefieras
            }
        }
    }

    fun openYoutubeUrl(context: Context, url: String) {
        val videoId = extractYoutubeVideoId(url)
        val youtubeUri = if (videoId != null) {
            Uri.parse("vnd.youtube:$videoId")
        } else {
            Uri.parse(url)
        }

        try {
            val intent = Intent(Intent.ACTION_VIEW, youtubeUri).apply {
                setPackage("com.google.android.youtube")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Necesario cuando se usa Application Context
            }
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private fun extractYoutubeVideoId(url: String): String? {
        val pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\\?video_id=|\\?v=|\\&v=|youtu.be\\/|watch\\?v=|\\/v\\/|\\/e\\/|embed\\/|youtu.be\\/|\\/v\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F)[^#\\&\\?\\n]*"
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(url)
        return if (matcher.find()) matcher.group() else null
    }

}