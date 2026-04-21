package com.pascal.catalog.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    @SerialName("image")
    val imageUrl: String,
    val price: Double,
    val rating: RatingDto,
)

@Serializable
data class RatingDto(
    val rate: Double,
    val count: Int,
)
