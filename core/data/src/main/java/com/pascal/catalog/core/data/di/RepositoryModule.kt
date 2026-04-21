package com.pascal.catalog.core.data.di

import com.pascal.catalog.core.data.repository.ProductRepositoryImpl
import com.pascal.catalog.core.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        repositoryImpl: ProductRepositoryImpl,
    ): ProductRepository
}
