package com.pegoraro.minimalisttracker.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.pegoraro.minimalisttracker.data.model.Food
import com.pegoraro.minimalisttracker.databinding.RvFoodListBinding
import com.pegoraro.minimalisttracker.ui.adapters.FoodListAdapter

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
