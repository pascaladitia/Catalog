package com.pascal.catalog.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pascal.catalog.core.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY isFavorite DESC, rating_rate DESC, title ASC")
    fun observeProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE title LIKE '%' || :query || '%' ORDER BY rating_rate DESC, title ASC")
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isFavorite = 1 ORDER BY title ASC")
    fun observeFavoriteProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE cartQuantity > 0 ORDER BY title ASC")
    fun observeCartProducts(): Flow<List<ProductEntity>>

    @Query("SELECT COALESCE(SUM(cartQuantity), 0) FROM products")
    fun observeCartCount(): Flow<Int>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    fun observeProduct(productId: Int): Flow<ProductEntity?>

    @Query("SELECT id FROM products WHERE isFavorite = 1")
    suspend fun getFavoriteIds(): List<Int>

    @Query("SELECT id, cartQuantity FROM products WHERE cartQuantity > 0")
    suspend fun getCartQuantities(): List<ProductCartQuantity>

    @Query("SELECT isFavorite FROM products WHERE id = :productId LIMIT 1")
    suspend fun isFavorite(productId: Int): Boolean?

    @Query("SELECT cartQuantity FROM products WHERE id = :productId LIMIT 1")
    suspend fun getCartQuantity(productId: Int): Int?

    @Upsert
    suspend fun upsertProducts(products: List<ProductEntity>)

    @Query("UPDATE products SET isFavorite = :isFavorite WHERE id = :productId")
    suspend fun updateFavorite(productId: Int, isFavorite: Boolean)

    @Query("UPDATE products SET cartQuantity = :quantity WHERE id = :productId")
    suspend fun updateCartQuantity(productId: Int, quantity: Int)

    @Query("UPDATE products SET cartQuantity = 0")
    suspend fun clearCart()
}

data class ProductCartQuantity(
    val id: Int,
    val cartQuantity: Int,
)
