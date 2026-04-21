package com.pascal.catalog.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pascal.catalog.core.database.dao.CategoryDao
import com.pascal.catalog.core.database.dao.OrderDao
import com.pascal.catalog.core.database.dao.ProductDao
import com.pascal.catalog.core.database.dao.UserDao
import com.pascal.catalog.core.database.entity.CategoryEntity
import com.pascal.catalog.core.database.entity.OrderEntity
import com.pascal.catalog.core.database.entity.OrderItemEntity
import com.pascal.catalog.core.database.entity.ProductEntity
import com.pascal.catalog.core.database.entity.UserEntity

@Database(
    entities = [
        ProductEntity::class,
        CategoryEntity::class,
        UserEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
    ],
    version = 2,
    exportSchema = true,
)
abstract class CatalogDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun userDao(): UserDao
    abstract fun orderDao(): OrderDao
}
