package com.pascal.catalog.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "favorite")
data class FavoritesEntity (
    @PrimaryKey(autoGenerate = false)
    val title: String,
    val isExclusive: Boolean? = null,
    val image: String? = null,
    val label: String? = null,
    val description: String? = null,
    val author: String? = null,
    val category: String? = null,
    val imageDescription: String? = null,
    val mediaCount: Int? = null,
    val publishedTime: String? = null,
    val audio: String? = null,
    val share: String? = null
)
