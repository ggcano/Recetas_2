package com.example.recetas_2.view

import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recetas_2.viewmodel.MainViewModel
import com.example.recetas_2.R
import com.example.recetas_2.databinding.ActivityMainBinding

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
    }

    private fun observeData() {
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
}