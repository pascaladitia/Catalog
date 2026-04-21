package com.pascal.catalog.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val imageUrl: String,
    val price: Double,
    val isFavorite: Boolean,
    val cartQuantity: Int,
    @Embedded(prefix = "rating_")
    val rating: RatingEmbedded,
)

data class RatingEmbedded(
    val rate: Double,
    val count: Int,
)
