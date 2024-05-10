package com.quinto.comicbook.data.local

import androidx.room.Entity
import java.util.Date

@Entity(primaryKeys = ["id", "itemType"])
data class ItemEntity(
    val id: Int,
    val itemType: String,
    val name: String? = null,
    val description: String? = null,
    val date: Date? = null,
    val thumbnailUrl: String? = null,
    val isFavorite: Boolean? = null,
    val updated: Date = Date()
)