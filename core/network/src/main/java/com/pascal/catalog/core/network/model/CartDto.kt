package com.pascal.catalog.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CartDto(
    val id: Int,
    val userId: Int,
    val date: String,
    val products: List<CartProductDto>,
)

@Serializable
data class CartProductDto(
    val productId: Int,
    val quantity: Int,
)
