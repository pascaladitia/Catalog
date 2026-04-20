package com.pascal.catalog.di

import androidx.room.Room
import com.pascal.catalog.data.local.database.AppDatabase
import com.pascal.catalog.data.local.database.DatabaseConstants
import com.pascal.catalog.data.local.repository.LocalRepository
import com.pascal.catalog.data.local.repository.LocalRepositoryImpl
import com.pascal.catalog.data.remote.api.KtorClientApi
import com.pascal.catalog.data.repository.NewsRepository
import com.pascal.catalog.data.repository.NewsRepositoryImpl
import com.pascal.catalog.domain.usecase.local.LocalUseCase
import com.pascal.catalog.domain.usecase.local.LocalUseCaseImpl
import com.pascal.catalog.domain.usecase.news.NewsUseCase
import com.pascal.catalog.domain.usecase.news.NewsUseCaseImpl
import com.pascal.catalog.ui.screen.bookmark.BookmarkViewModel
import com.pascal.catalog.ui.screen.detail.DetailViewModel
import com.pascal.catalog.ui.screen.favorite.FavoriteViewModel
import com.pascal.catalog.ui.screen.home.HomeViewModel
import com.pascal.catalog.ui.screen.profile.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {

    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DatabaseConstants.DB_NAME
        )
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    // Data source
    singleOf(::LocalRepositoryImpl) { bind<LocalRepository>() }

    // API client
    singleOf(::KtorClientApi)

    // Repository
    singleOf(::NewsRepositoryImpl) { bind<NewsRepository>() }

    // UseCases
    singleOf(::LocalUseCaseImpl) { bind<LocalUseCase>() }
    singleOf(::NewsUseCaseImpl) { bind<NewsUseCase>() }

    // ViewModels
    viewModelOf(::HomeViewModel)
    viewModelOf(::FavoriteViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::DetailViewModel)
    viewModelOf(::BookmarkViewModel)
}
