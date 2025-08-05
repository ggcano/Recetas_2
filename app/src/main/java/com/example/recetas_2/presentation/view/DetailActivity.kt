package com.example.recetas_2.presentation.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.recetas_2.R
import com.example.recetas_2.databinding.ActivityDetailBinding
import com.example.recetas_2.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityRetainedScoped

@ActivityRetainedScoped
@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: MainViewModel by viewModels()

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
        viewModel.loading.observe(this) { isLoading ->
            binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE

        }
        viewModel.detailMeals.observe(this) {
              setupToolbar(it.name)
            binding.textDetailDescription.text = it.instructions
            binding.youtubeDetailUrl.text = it.urlYoutube
            Glide.with(this@DetailActivity)
                .load(it.thumbnail)
                .circleCrop()
                .into(binding.imageDetailThhumb)


        }

        viewModel.error.observe(this) { error ->
            error?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
    }

    fun getOnClicks() {
        binding.youtubeDetailUrl.setOnClickListener {
            viewModel.openYoutubeUrl(this@DetailActivity, binding.youtubeDetailUrl.text.toString())
        }
    }

    private fun setupToolbar(message:String) {

        setSupportActionBar(binding.toolbar)

        // Habilitar el botón de retroceso
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = message
        // O para título en mayúsculas (como en Material Design):
        // toolbar.title = "MI TÍTULO"

        // Opcional: Cambiar color del texto del título
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Opcional: quitar el título
       // supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}