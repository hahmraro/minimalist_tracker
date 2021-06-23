package com.example.elegantcalorietracker.utils

import com.example.elegantcalorietracker.data.model.Food

fun List<Food>.sum(): Food = takeIf { it.isNotEmpty() }
    ?.reduce { acc: Food, food: Food -> acc + food } ?: Food()
