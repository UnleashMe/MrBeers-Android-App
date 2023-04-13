package com.example.neverpidor.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.neverpidor.data.database.type_converters.CategoryConverter
import com.example.neverpidor.data.providers.MenuCategory

@Entity(tableName = "items")
@TypeConverters(CategoryConverter::class)
data class MenuItemEntity(
    @PrimaryKey(autoGenerate = false)
    val UID: String,
    val name: String,
    val description: String,
    val type: String,
    val price: Double,
    val alcPercentage: Double?,
    val volume: Double?,
    val isInCart: Boolean = false,
    val isFaved: Boolean = false,
    val category: MenuCategory
)
