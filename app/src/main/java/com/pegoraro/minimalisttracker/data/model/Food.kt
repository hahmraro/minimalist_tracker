package com.pegoraro.minimalisttracker.data.model

import androidx.recyclerview.widget.RecyclerView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pegoraro.minimalisttracker.data.api.CalorieNinjasService
import com.pegoraro.minimalisttracker.data.database.FoodDatabase
import com.squareup.moshi.Json
import kotlin.random.Random

/**
 * Enum class that contains all the possible [Food] list types used by the app
 */
enum class ListType { BREAKFAST, LUNCH, DINNER, SNACKS, HISTORY }

/**
 * Alias for the [Pair]<*Nutrient name*, *Nutrient value*> used by nutrient
 * lists throughout the app
 */
typealias Nutrient = Pair<String, Double>

/**
 * Needs to exists because the returning json file from the API only has one
 * key called "items", which is a list of food
 */
data class FoodList(val items: List<Food>)

/**
 * The domain model of the app, as well as the [Entity] of the [FoodDatabase]
 *
 * @property id The unique ID of the object. Used to differentiate it from other
 * [Food] objects both from the [FoodDatabase] and from the various [RecyclerView]
 * adapters used by the app
 * @property listType The [ListType.ordinal] of the food. Used to determine on which
 * list this [Food] object should be placed in the app
 * @property name Its name. Retrieved from the "name" key from the [CalorieNinjasService]
 * json file. Used together with its [id] to differentiate itself from other [Food]
 * objects from the various [RecyclerView] adapters used by the app
 */
@Entity(tableName = "saved_foods_table")
data class Food(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = Random.nextInt(),

    @ColumnInfo(name = "list_type")
    var listType: Int = ListType.BREAKFAST.ordinal,

    @ColumnInfo(name = "name")
    val name: String = "",

    /* Food Nutrients */
    @ColumnInfo(name = "serving_size")
    @Json(name = "serving_size_g")
    var servingSize: String = "0.0",

    @ColumnInfo(name = "calories")
    var calories: String = "0.0",

    @ColumnInfo(name = "sugar")
    @Json(name = "sugar_g")
    var sugar: String = "0.0",

    @ColumnInfo(name = "fiber")
    @Json(name = "fiber_g")
    var fiber: String = "0.0",

    @ColumnInfo(name = "carbs")
    @Json(name = "carbohydrates_total_g")
    var totalCarbs: String = "0.0",

    @ColumnInfo(name = "saturated_fat")
    @Json(name = "fat_saturated_g")
    var saturatedFat: String = "0.0",

    @ColumnInfo(name = "fat")
    @Json(name = "fat_total_g")
    var totalFat: String = "0.0",

    @ColumnInfo(name = "protein")
    @Json(name = "protein_g")
    var protein: String = "0.0",

    @ColumnInfo(name = "sodium")
    @Json(name = "sodium_mg")
    var sodium: String = "0.0",

    @ColumnInfo(name = "potassium")
    @Json(name = "potassium_mg")
    var potassium: String = "0.0",

    @ColumnInfo(name = "cholesterol")
    @Json(name = "cholesterol_mg")
    var cholesterol: String = "0.0",

) {

    /**
     * Plus operator (+) function that lets two or more [Food] objets be added,
     * in which the operation will return a new [Food] object containing the sum
     * of all its nutrients
     *
     * @param food The [Food] object to do the operation on
     */
    @Override
    operator fun plus(food: Food) =
        Food(
            calories = (this.calories.toDouble() + food.calories.toDouble()).toString(),
            fiber = (this.fiber.toDouble() + food.fiber.toDouble()).toString(),
            sugar = (this.sugar.toDouble() + food.sugar.toDouble()).toString(),
            totalCarbs = (this.totalCarbs.toDouble() + food.totalCarbs.toDouble()).toString(),
            saturatedFat = (this.saturatedFat.toDouble() + food.saturatedFat.toDouble()).toString(),
            totalFat = (this.totalFat.toDouble() + food.totalFat.toDouble()).toString(),
            protein = (this.protein.toDouble() + food.protein.toDouble()).toString(),
            sodium = (this.sodium.toDouble() + food.sodium.toDouble()).toString(),
            potassium = (this.potassium.toDouble() + food.potassium.toDouble()).toString(),
            cholesterol = (this.cholesterol.toDouble() + food.cholesterol.toDouble()).toString(),
        )

    /**
     * Returns a [List] containing all the [Food] nutrients, each converted to
     * [Double]
     *
     * @return a [List] of [Double]
     */
    fun getNutrients() = listOf(
        calories.toDouble(),
        sugar.toDouble(),
        fiber.toDouble(),
        totalCarbs.toDouble(),
        saturatedFat.toDouble(),
        totalFat.toDouble(),
        protein.toDouble(),
        sodium.toDouble(),
        potassium.toDouble(),
        cholesterol.toDouble()
    )

    /**
     * Modifies the [Food] nutrients based on a new [servingSize] and [listType]
     *
     * Calculates the ratio between the old [Food.servingSize] and the new
     * one, and multiply each [Food] nutrient against it
     *
     * @param newServingSize a [Double] representing the new [Food.servingSize]
     * of which the ratio will be calculated from
     * @param newListType Optional [Int] that represents a [ListType.ordinal]
     * that replaces the current [Food.listType]
     * @return the modified [Food]
     */
    fun edit(newServingSize: Double, newListType: ListType? = null):
        Food {
        listType = newListType?.ordinal ?: listType
        val ratio = newServingSize / servingSize.toDouble()
        servingSize = (servingSize.toDouble() * ratio).toString()
        calories = (calories.toDouble() * ratio).toString()
        sugar = (sugar.toDouble() * ratio).toString()
        fiber = (fiber.toDouble() * ratio).toString()
        totalCarbs = (totalCarbs.toDouble() * ratio).toString()
        saturatedFat = (saturatedFat.toDouble() * ratio).toString()
        totalFat = (totalFat.toDouble() * ratio).toString()
        protein = (protein.toDouble() * ratio).toString()
        sodium = (sodium.toDouble() * ratio).toString()
        potassium = (potassium.toDouble() * ratio).toString()
        cholesterol = (cholesterol.toDouble() * ratio).toString()
        return this
    }
}
