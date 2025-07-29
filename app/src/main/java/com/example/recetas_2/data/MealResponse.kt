package com.example.recetas_2.data

import com.example.recetas_2.data.model.Meal
import com.google.gson.annotations.SerializedName

data class MealResponse(
    @SerializedName("meals") val meals: List<Meal>?
)