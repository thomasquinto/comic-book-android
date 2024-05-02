package com.quinto.comicbook.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ItemEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ComicBookDatabase: RoomDatabase() {
    abstract val dao: ItemDao
}