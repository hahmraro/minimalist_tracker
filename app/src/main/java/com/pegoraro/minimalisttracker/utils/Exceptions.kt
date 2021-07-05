package com.pegoraro.minimalisttracker.utils

class NoConnectionException : Exception() {
    override fun toString() = "Could not connect to the server"
}

class FoodNotFoundException : Exception() {
    override fun toString() = "Food not found"
}
