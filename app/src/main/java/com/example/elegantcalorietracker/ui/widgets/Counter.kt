package com.example.elegantcalorietracker.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.elegantcalorietracker.databinding.ClCounterBinding

class Counter(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private val binding: ClCounterBinding = ClCounterBinding.inflate(
        LayoutInflater.from(context), this, true
    )
}
