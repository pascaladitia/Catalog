package com.pascal.catalog.core.data.mapper

import com.pascal.catalog.core.database.dao.ProductCartQuantity
import com.pascal.catalog.core.database.entity.CategoryEntity
import com.pascal.catalog.core.database.entity.OrderEntity
import com.pascal.catalog.core.database.entity.OrderItemEntity
import com.pascal.catalog.core.database.entity.OrderWithItems
import com.pascal.catalog.core.database.entity.ProductEntity
import com.pascal.catalog.core.database.entity.RatingEmbedded
import com.pascal.catalog.core.database.entity.UserEntity
import com.pascal.catalog.core.domain.model.CatalogUser
import com.pascal.catalog.core.domain.model.CategorySummary
import com.pascal.catalog.core.domain.model.Order
import com.pascal.catalog.core.domain.model.OrderLine
import com.pascal.catalog.core.domain.model.Product
import com.pascal.catalog.core.domain.model.Rating
import com.pascal.catalog.core.network.model.CartDto
import com.pascal.catalog.core.network.model.ProductDto
import com.pascal.catalog.core.network.model.UserDto

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
    cartQuantity = cartQuantity,
)

fun ProductDto.toEntity(
    isFavorite: Boolean,
    cartQuantity: Int,
): ProductEntity = ProductEntity(
    id = id,
    title = title,
    description = description,
    category = category,
    imageUrl = imageUrl,
    price = price,
    isFavorite = isFavorite,
    cartQuantity = cartQuantity,
    rating = RatingEmbedded(
        rate = rating.rate,
        count = rating.count,
    ),
)

fun List<String>.toCategoryEntities(): List<CategoryEntity> = map(::CategoryEntity)

fun CategoryEntity.toDomain(itemCount: Int): CategorySummary = CategorySummary(
    name = name,
    itemCount = itemCount,
)

fun UserDto.toEntity(communitySize: Int): UserEntity = UserEntity(
    id = id,
    email = email,
    username = username,
    firstName = name.firstname,
    lastName = name.lastname,
    phone = phone,
    city = address.city,
    street = "${address.number} ${address.street}",
    zipCode = address.zipcode,
    communitySize = communitySize,
)

fun UserEntity.toDomain(): CatalogUser = CatalogUser(
    id = id,
    fullName = "$firstName $lastName",
    username = username,
    email = email,
    phone = phone,
    city = city,
    street = street,
    zipCode = zipCode,
    communitySize = communitySize,
)

fun CartDto.toOrderEntity(): OrderEntity = OrderEntity(
    id = id,
    userId = userId,
    date = date,
)

fun CartDto.toOrderItemEntities(): List<OrderItemEntity> = products.map { item ->
    OrderItemEntity(
        orderId = id,
        productId = item.productId,
        quantity = item.quantity,
    )
}

fun OrderWithItems.toDomain(products: Map<Int, ProductEntity>): Order {
    val lines = items.map { item ->
        val product = products[item.productId]
        val price = product?.price ?: 0.0
        OrderLine(
            productId = item.productId,
            productTitle = product?.title ?: "Product #${item.productId}",
            quantity = item.quantity,
            lineTotal = price * item.quantity,
        )
    }
    return Order(
        id = order.id,
        date = order.date,
        itemCount = lines.sumOf { it.quantity },
        totalAmount = lines.sumOf { it.lineTotal },
        items = lines,
    )
}

fun List<ProductCartQuantity>.toCartQuantityMap(): Map<Int, Int> = associate { it.id to it.cartQuantity }
