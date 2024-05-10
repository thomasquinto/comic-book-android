package com.quinto.comicbook.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class ItemEntity(
    @PrimaryKey val id: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val date: Date? = null,
    val thumbnailUrl: String? = null,
    val itemType: String? = null,
    val isFavorite: Boolean? = null,
    val updated: Date = Date()
)