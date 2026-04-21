package com.pascal.catalog.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.pascal.catalog.core.database.entity.OrderEntity
import com.pascal.catalog.core.database.entity.OrderItemEntity
import com.pascal.catalog.core.database.entity.OrderWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Transaction
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY date DESC")
    fun observeOrders(userId: Int): Flow<List<OrderWithItems>>

    @Upsert
    suspend fun upsertOrders(orders: List<OrderEntity>)

    @Upsert
    suspend fun upsertOrderItems(orderItems: List<OrderItemEntity>)

    @Query("DELETE FROM order_items WHERE orderId IN (SELECT id FROM orders WHERE userId = :userId)")
    suspend fun deleteOrderItemsForUser(userId: Int)

    @Query("DELETE FROM orders WHERE userId = :userId")
    suspend fun deleteOrdersForUser(userId: Int)
}
