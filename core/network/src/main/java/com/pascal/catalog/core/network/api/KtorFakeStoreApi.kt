package com.pascal.catalog.core.network.api

import com.pascal.catalog.core.network.model.CartDto
import com.pascal.catalog.core.network.model.ProductDto
import com.pascal.catalog.core.network.model.UserDto
import com.pascal.catalog.core.network.model.login.LoginBody
import com.pascal.catalog.core.network.model.login.LoginDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class KtorFakeStoreApi @Inject constructor(
    private val client: HttpClient,
) : FakeStoreApi {

    override suspend fun login(body: LoginBody): LoginDto =
        client.post("auth/login") {
            setBody(body)
        }.body()

    override suspend fun getProducts(): List<ProductDto> =
        client.get("products").body()

    override suspend fun getProduct(productId: Int): ProductDto =
        client.get("products/$productId").body()

    override suspend fun getCategories(): List<String> =
        client.get("products/categories").body()

    override suspend fun getProductsByCategory(category: String): List<ProductDto> =
        client.get("products/category/$category").body()

    override suspend fun getUsers(): List<UserDto> =
        client.get("users").body()

    override suspend fun getUser(userId: Int): UserDto =
        client.get("users/$userId").body()

    override suspend fun getOrdersByUser(userId: Int): List<CartDto> =
        client.get("carts/user/$userId").body()
}
