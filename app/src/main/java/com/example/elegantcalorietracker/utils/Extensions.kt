package com.example.elegantcalorietracker.utils

import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.data.model.ListType

fun List<Food>.sum(): Food = takeIf { it.isNotEmpty() }
    ?.reduce { acc: Food, food: Food -> acc + food } ?: Food()

fun List<Food>.changeListType(listType: ListType) = map { food ->
    food.copy(listType = listType.ordinal)
}
