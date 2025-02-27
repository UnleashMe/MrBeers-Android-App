package com.example.beers_app.data.database.type_converters

import androidx.room.TypeConverter
import com.example.beers_app.data.common.MenuCategory

class CategoryConverter {

    @TypeConverter
    fun categoryToString(category: MenuCategory): String {
        return when (category) {
            MenuCategory.BeerCategory -> "Beer"
            MenuCategory.SnackCategory -> "Snack"
        }
    }

    @TypeConverter
    fun stringToCategory(s: String): MenuCategory {
        return when {
            s.contains("Beer") -> MenuCategory.BeerCategory
            s.contains("Snack") -> MenuCategory.SnackCategory
            else -> throw Exception("Unknown category")
        }
    }
}