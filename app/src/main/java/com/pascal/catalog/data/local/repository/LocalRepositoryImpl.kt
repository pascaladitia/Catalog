package com.pascal.catalog.data.local.repository

import com.pascal.catalog.data.local.database.AppDatabase
import com.pascal.catalog.data.local.entity.FavoritesEntity
import com.pascal.catalog.data.local.entity.ProfileEntity
import org.koin.core.annotation.Single

@Single
class LocalRepositoryImpl(
    private val database: AppDatabase,
) : LocalRepository {

    // Profile
    override suspend fun getProfileById(id: Long): ProfileEntity? {
        return database.profileDao().getProfileById(id)
    }

    override suspend fun getAllProfiles(): List<ProfileEntity> {
        return database.profileDao().getAllProfiles()
    }

    override suspend fun deleteProfileById(item: ProfileEntity) {
        return database.profileDao().deleteProfile(item)
    }

    override suspend fun insertProfile(item: ProfileEntity) {
        return database.profileDao().insertProfile(item)
    }

    // Favorites
    override suspend fun insertFavorite(entity: FavoritesEntity) {
        database.favoritesDao().insertFavorite(entity)
    }

    override suspend fun deleteFavorite(entity: FavoritesEntity) {
        database.favoritesDao().deleteFavorite(entity)
    }

    override suspend fun getFavorite(): List<FavoritesEntity>? {
        return database.favoritesDao().getFavoriteMovieList()
    }

    override suspend fun getFavorite(title: String): Boolean {
        return database.favoritesDao().getFavorite(title) != null
    }

    override suspend fun clearFavorite() {
        return database.favoritesDao().clearFavoritesTable()
    }
}