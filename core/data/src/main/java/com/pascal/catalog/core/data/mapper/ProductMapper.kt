package com.pascal.catalog.core.data.mapper

import com.pascal.catalog.core.database.entity.ProductEntity
import com.pascal.catalog.core.database.entity.RatingEmbedded
import com.pascal.catalog.core.domain.model.Product
import com.pascal.catalog.core.domain.model.Rating
import com.pascal.catalog.core.network.model.ProductDto

fun ProductEntity.toDomain(): Product = Product(
    id = id,
    title = title,
    description = description,
    category = category,
    imageUrl = imageUrl,
    price = price,
    rating = Rating(
        rate = rating.rate,
        count = rating.count,
    ),
    isFavorite = isFavorite,
)

fun ProductDto.toEntity(isFavorite: Boolean): ProductEntity = ProductEntity(
    id = id,
    title = title,
    description = description,
    category = category,
    imageUrl = imageUrl,
    price = price,
    isFavorite = isFavorite,
    rating = RatingEmbedded(
        rate = rating.rate,
        count = rating.count,
    ),
)
