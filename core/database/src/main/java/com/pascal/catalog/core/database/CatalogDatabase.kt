package com.pascal.catalog.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pascal.catalog.core.database.dao.ProductDao
import com.pascal.catalog.core.database.entity.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class CatalogDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
