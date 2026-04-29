package com.pascal.catalog.core.network.di

import com.pascal.catalog.core.network.api.FakeStoreApi
import com.pascal.catalog.core.network.api.KtorFakeStoreApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideHttpClient(json: Json): HttpClient =
        KtorClientFactory.create(json)

    @Provides
    @Singleton
    fun provideFakeStoreApi(api: KtorFakeStoreApi): FakeStoreApi = api
}
