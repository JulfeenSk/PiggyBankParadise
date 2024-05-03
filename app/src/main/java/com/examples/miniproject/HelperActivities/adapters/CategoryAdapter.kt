package com.examples.miniproject.HelperActivities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.examples.miniproject.HelperActivities.models.Category
import com.examples.miniproject.R
import com.examples.miniproject.databinding.SampleCategoryItemBinding

class CategoryAdapter(
    private val context: Context,
    private val categories: ArrayList<Category>,
    private val categoryClickListener: CategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface CategoryClickListener {
        fun onCategoryClicked(category: Category)
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = SampleCategoryItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.sample_category_item, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.categoryText.text = category.categoryName
        holder.binding.categoryIcon.setImageResource(category.categoryImage)
        holder.binding.categoryIcon.backgroundTintList = ContextCompat.getColorStateList(context, category.categoryColor)

        holder.itemView.setOnClickListener {
            categoryClickListener.onCategoryClicked(category)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}
