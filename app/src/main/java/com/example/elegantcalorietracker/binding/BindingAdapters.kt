package com.example.elegantcalorietracker

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.ui.adapters.FoodListAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: MutableList<Food>?) {
    val adapter = recyclerView.adapter as FoodListAdapter
    adapter.submitList(data)
}
