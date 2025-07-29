package com.example.recetas_2.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.recetas_2.databinding.ItemMealBinding
import com.example.recetas_2.data.model.Meal

class MealsAdapter : RecyclerView.Adapter<MealsAdapter.MealViewHolder>() {
    private var meals = emptyList<Meal>()
    private var onItemClickListener: ((String) -> Unit)? = null


    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    fun submitList(newList: List<Meal>) {
        meals = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemMealBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(meals[position])
    }

    override fun getItemCount() = meals.size

    inner class MealViewHolder(private val binding: ItemMealBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(meal: Meal) {
            binding.textMealName.text = meal.name
            Glide.with(binding.root)
                .load(meal.thumbnail)
                .into(binding.imageMeal)

            // Configurar el click listener en el CardView o ConstraintLayout
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(meal.id)
            }
        }
    }
}