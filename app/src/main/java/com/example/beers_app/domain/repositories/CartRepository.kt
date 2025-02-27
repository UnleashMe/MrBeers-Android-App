package com.example.beers_app.domain.repositories

import com.example.beers_app.data.database.entities.UserAndItemsInCart
import com.example.beers_app.data.database.entities.UserMenuItemCart
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    suspend fun addItemInCart(userMenuItemCart: UserMenuItemCart)

    suspend fun updateItemInCart(userMenuItemCart: UserMenuItemCart)

    suspend fun removeItemFromCart(number: String, id: String)

     fun getUserCartByNumber(number: String): Flow<UserAndItemsInCart>

    suspend fun getItemInCart(number: String, id: String): UserMenuItemCart?

    suspend fun clearUserCart(number: String)
}