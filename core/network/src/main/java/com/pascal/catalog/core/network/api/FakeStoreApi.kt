package com.pascal.catalog.core.network.api

import com.pascal.catalog.core.network.model.CartDto
import com.pascal.catalog.core.network.model.ProductDto
import com.pascal.catalog.core.network.model.UserDto
import com.pascal.catalog.core.network.model.login.LoginBody
import com.pascal.catalog.core.network.model.login.LoginDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FakeStoreApi {
    @POST("auth/login")
    suspend fun login(@Body body: LoginBody): LoginDto

    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") productId: Int): ProductDto

    @GET("products/categories")
    suspend fun getCategories(): List<String>

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): List<ProductDto>

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: Int): UserDto

    @GET("carts/user/{id}")
    suspend fun getOrdersByUser(@Path("id") userId: Int): List<CartDto>
}
