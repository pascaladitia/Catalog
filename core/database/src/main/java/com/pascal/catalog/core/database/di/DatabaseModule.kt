package com.pascal.catalog.core.database.di

import android.content.Context
import androidx.room.Room
import com.pascal.catalog.core.database.CatalogDatabase
import com.pascal.catalog.core.database.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCatalogDatabase(
        @ApplicationContext context: Context,
    ): CatalogDatabase = Room.databaseBuilder(
        context,
        CatalogDatabase::class.java,
        "catalog.db",
    ).fallbackToDestructiveMigration(dropAllTables = true).build()

    @Provides
    fun provideProductDao(database: CatalogDatabase): ProductDao = database.productDao()
}
