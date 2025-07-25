package com.example.recetas_2.model

import com.google.gson.annotations.SerializedName

data class Meal(
    @SerializedName("idMeal") val id: String,
    @SerializedName("strMeal") val name: String,
    @SerializedName("strMealThumb") val thumbnail: String,
    @SerializedName("strInstructions") val instructions: String?,
    @SerializedName("strYoutube") val urlYoutube: String?
)