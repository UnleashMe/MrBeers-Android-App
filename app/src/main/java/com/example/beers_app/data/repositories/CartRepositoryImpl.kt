package com.example.beers_app.data.repositories

import com.example.beers_app.data.database.CartDao
import com.example.beers_app.data.database.entities.UserAndItemsInCart
import com.example.beers_app.data.database.entities.UserMenuItemCart
import com.example.beers_app.domain.repositories.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
): CartRepository {
    override suspend fun addItemInCart(userMenuItemCart: UserMenuItemCart) {
        cartDao.addItemInCart(userMenuItemCart)
    }

    override suspend fun updateItemInCart(userMenuItemCart: UserMenuItemCart) {
        cartDao.updateItemInCart(userMenuItemCart)
    }

    override suspend fun removeItemFromCart(number: String, id: String) {
        cartDao.removeItemFromCart(number, id)
    }

    override  fun getUserCartByNumber(number: String): Flow<UserAndItemsInCart> {
       return cartDao.getUsersCartByNumber(number)
    }

    override suspend fun getItemInCart(number: String, id: String): UserMenuItemCart? {
        return cartDao.getItemInCart(number, id)
    }

    override suspend fun clearUserCart(number: String) {
        cartDao.clearUserCart(number)
    }
}