package com.quinto.comicbook.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(
        comicEntities: List<ItemEntity>
    )

    @Query("DELETE FROM itementity")
    suspend fun clearItems()

    // Changed from this to be a "starts with" search
    //            WHERE LOWER(title) LIKE '%' || LOWER(:query) || '%'
    @Query(
        """
            SELECT * 
            FROM itementity
            WHERE LOWER(name) LIKE LOWER(:query) || '%'
            ORDER BY (:orderBy)
        """
    )
    suspend fun searchItems(query: String, orderBy: String): List<ItemEntity>
}