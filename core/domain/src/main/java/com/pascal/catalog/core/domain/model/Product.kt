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
)

data class Rating(
    val rate: Double,
    val count: Int,
)
