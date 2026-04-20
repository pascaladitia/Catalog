package com.pascal.catalog.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pascal.catalog.data.local.entity.FavoritesEntity

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertFavorite(cachedTest: FavoritesEntity) : Long

    @Delete
    abstract suspend fun deleteFavorite(item: FavoritesEntity) : Int

    @Query("SELECT count(*) FROM favorite")
    abstract suspend fun countTests(): Int

    @Query("SELECT * FROM favorite")
    abstract suspend fun getFavoriteMovieList(): List<FavoritesEntity>?

    @Query("SELECT * FROM favorite WHERE LOWER(title) = LOWER(:title) LIMIT 1")
    suspend fun getFavorite(title: String): FavoritesEntity?

    @Query("DELETE FROM favorite")
    abstract suspend fun clearFavoritesTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllFavorites(cachedTests: List<FavoritesEntity>) : List<Long>

}