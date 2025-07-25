package com.example.recetas_2.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.recetas_2.R
import com.example.recetas_2.databinding.ActivityDetailBinding
import com.example.recetas_2.viewmodel.MainViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: MainViewModel by viewModels()
    private var url1: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Inicialización del binding
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mealId = intent.getStringExtra("MEAL_ID") ?: "52772"
        viewModel.detailMealsQuery(mealId)
        observeData()
        getOnClicks()
    }

    private fun observeData() {
        viewModel.detailMeals.observe(this) {
            binding.textDetailTitle.text = it.name
            binding.textDetailDescription.text = it.instructions
            binding.youtubeDetailUrl.text = it.urlYoutube
            Glide.with(this@DetailActivity)
                .load(it.thumbnail)
                .into(binding.imageDetailThhumb)


        }

        viewModel.error.observe(this) { error ->
            error?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
    }

    fun getOnClicks(){
       binding.youtubeDetailUrl.setOnClickListener {
           viewModel.openYoutubeUrl(this@DetailActivity,binding.youtubeDetailUrl.text.toString())
       }
    }

}