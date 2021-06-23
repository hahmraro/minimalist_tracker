package com.example.elegantcalorietracker.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.databinding.ItemFoodBinding

typealias FoodClickListener = (Food) -> Unit
typealias FoodLongClickListener = (PopupMenu, Food, View) -> Boolean

class FoodListAdapter(
    private val clickListener: FoodClickListener? = null,
    private val longClickListener: FoodLongClickListener? = null
) :
    ListAdapter<Food, FoodListAdapter.FoodViewHolder>(DiffCallback) {

    class FoodViewHolder(
        private var binding: ItemFoodBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(food: Food) {
            binding.foodName.text =
                food.name.replaceFirstChar { it.titlecase() }
            binding.foodCalories.text = "${food.calories} kcal"
            binding.servingSize.text = "${food.servingSize} g"
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of
     * [Food] has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(
            oldItem: Food,
            newItem: Food
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Food,
            newItem: Food
        ): Boolean {
            return oldItem.servingSize == newItem.servingSize
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoodViewHolder {
        val view = ItemFoodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoodViewHolder(view)
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = getItem(position)
        if (clickListener != null) {
            holder.itemView.setOnClickListener { clickListener!!(food) }
        }
        if (longClickListener != null) {
            holder.itemView.setOnLongClickListener {
                val menu = PopupMenu(it.context, it)
                longClickListener!!(menu, food, it)
                menu.show()
                true
            }
        }
        holder.bind(food)
    }
}
