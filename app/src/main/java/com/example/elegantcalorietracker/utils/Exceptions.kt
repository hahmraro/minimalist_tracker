package com.example.elegantcalorietracker.utils

class NoConnection : Exception() {
    override fun toString() = "Could not connect to the server"
}

class FoodNotFound : Exception() {
    override fun toString() = "Food not found"
}
