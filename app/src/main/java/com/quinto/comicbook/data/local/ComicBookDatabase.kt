package com.quinto.comicbook.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ItemEntity::class, ItemRequest::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ComicBookDatabase: RoomDatabase() {
    abstract val itemDao: ItemDao
    abstract val itemRequestDao: ItemRequestDao
}