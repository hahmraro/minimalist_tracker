package com.example.elegantcalorietracker.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.databinding.RvFoodListBinding
import com.example.elegantcalorietracker.ui.adapters.FoodListAdapter

class FoodListView(
    context: Context,
    attributeSet: AttributeSet?
) : LinearLayoutCompat(context, attributeSet) {

    private val binding: RvFoodListBinding = RvFoodListBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun setListData(data: List<Food>?) {
        val adapter = binding.listRecycler.adapter as FoodListAdapter
        adapter.submitList(data)
    }

    fun setButtonClickListener(buttonClickListener: OnClickListener) {
        binding.listButton.setOnClickListener(buttonClickListener)
    }

    fun setListTitle(listText: String) {
        binding.listText.text = listText
    }

    fun setAdapter(adapter: FoodListAdapter) {
        binding.listRecycler.adapter = adapter
    }
}
