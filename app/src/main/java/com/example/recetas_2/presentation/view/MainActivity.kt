package com.example.recetas_2.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recetas_2.presentation.MainViewModel
import com.example.recetas_2.R
import com.example.recetas_2.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityRetainedScoped

@ActivityRetainedScoped
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización del binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearch()
        observeData()
        goToDetail()
    }

    private fun observeData() {
        viewModel.loading.observe(this) { isLoading ->
            binding.progressBarMain.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.editTextSearchMain.isEnabled = !isLoading
        }
        viewModel.meals.observe(this) {
            // Actualizar RecyclerView
            (binding.recyclerView.adapter as MealsAdapter).submitList(it)
        }

        viewModel.error.observe(this) { error ->
            error?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
    }


    private fun setupSearch() {
        // Configuración del SearchView
        binding.editTextSearchMain.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    performSearch(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = false
            })
        }

        // Configuración del botón
        binding.buttonActionMain.setOnClickListener {
            val query = binding.editTextSearchMain.query.toString()
            performSearch(query)
        }
    }

    // Función reutilizable para ambas acciones
    private fun performSearch(query: String?) {
        query?.takeIf { it.isNotBlank() }?.let {
            viewModel.searchMeals(it)
            binding.editTextSearchMain.clearFocus() // Oculta el teclado
        } ?: run {
            Toast.makeText(this, getString(R.string.search_error_word), Toast.LENGTH_LONG).show()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = MealsAdapter()
        }
    }

    private fun goToDetail(){
        val adapter = MealsAdapter()
        binding.recyclerView.adapter = adapter
        adapter.setOnItemClickListener { idMeal ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("MEAL_ID", idMeal)
            }
            startActivity(intent)
        }
    }
}