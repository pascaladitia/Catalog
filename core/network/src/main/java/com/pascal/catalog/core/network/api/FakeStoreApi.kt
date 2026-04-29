package com.pascal.catalog.core.network.api

import com.pascal.catalog.core.network.model.CartDto
import com.pascal.catalog.core.network.model.ProductDto
import com.pascal.catalog.core.network.model.UserDto
import com.pascal.catalog.core.network.model.login.LoginBody
import com.pascal.catalog.core.network.model.login.LoginDto

interface FakeStoreApi {
    suspend fun login(body: LoginBody): LoginDto

    suspend fun getProducts(): List<ProductDto>

    suspend fun getProduct(productId: Int): ProductDto

    suspend fun getCategories(): List<String>

    suspend fun getProductsByCategory(category: String): List<ProductDto>

    suspend fun getUsers(): List<UserDto>

    suspend fun getUser(userId: Int): UserDto

    suspend fun getOrdersByUser(userId: Int): List<CartDto>
}
