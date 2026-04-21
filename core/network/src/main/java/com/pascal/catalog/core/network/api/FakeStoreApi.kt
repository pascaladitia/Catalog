package com.pascal.catalog.core.network.api

import com.pascal.catalog.core.network.model.ProductDto
import retrofit2.http.GET

interface FakeStoreApi {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>
}
