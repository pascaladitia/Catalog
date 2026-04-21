package com.pascal.catalog.core.domain.model

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val imageUrl: String,
    val price: Double,
    val rating: Rating,
    val isFavorite: Boolean,
    val cartQuantity: Int,
)

data class Rating(
    val rate: Double,
    val count: Int,
)

data class CatalogUser(
    val id: Int,
    val fullName: String,
    val username: String,
    val email: String,
    val phone: String,
    val city: String,
    val street: String,
    val zipCode: String,
    val communitySize: Int,
)

data class CategorySummary(
    val name: String,
    val itemCount: Int,
)

data class Order(
    val id: Int,
    val date: String,
    val itemCount: Int,
    val totalAmount: Double,
    val items: List<OrderLine>,
)

data class OrderLine(
    val productId: Int,
    val productTitle: String,
    val quantity: Int,
    val lineTotal: Double,
)
