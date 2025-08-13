package com.example.recetas_2.presentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recetas_2.data.MealRepository
import com.example.recetas_2.data.Result
import com.example.recetas_2.data.model.Meal
import com.example.recetas_2.usecase.DetailMealUseCase
import com.example.recetas_2.usecase.SearchMealsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(private val repository: MealRepository) : ViewModel() {


    private val searchMealsUseCase = SearchMealsUseCase(repository)
    private val detailMealUseCase = DetailMealUseCase(repository)
    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading
    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>> = _meals

    private val _detailsMeals = MutableLiveData<Meal>()
    val detailMeals: LiveData<Meal> = _detailsMeals

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun searchMeals(query: String) {
        _loading.value = true
        viewModelScope.launch {
            when (val result = searchMealsUseCase(query)) {
                is Result.Success -> {
                    _error.value = null
                    _meals.value = result.data
                }

                is Result.Error -> {
                    _error.value = result.exception.message
                    _meals.value = emptyList()
                }
            }
            _loading.value = false
        }

    }

    fun detailMealsQuery(query: String) {
        _loading.value = true
        viewModelScope.launch {
            when (val result = detailMealUseCase(query)) {
                is Result.Success -> {
                    _error.value = null
                    _detailsMeals.value = result.data
                }

                is Result.Error -> {
                    _error.value = result.exception.message
                    _detailsMeals.value = null
                }
            }
            _loading.value = false
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
        val pattern =
            "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\\?video_id=|\\?v=|\\&v=|youtu.be\\/|watch\\?v=|\\/v\\/|\\/e\\/|embed\\/|youtu.be\\/|\\/v\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F)[^#\\&\\?\\n]*"
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(url)
        return if (matcher.find()) matcher.group() else null
    }

}