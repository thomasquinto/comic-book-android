package com.quinto.comicbook.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.quinto.comicbook.data.remote.OrderBy

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

fun toDbOrderBy(orderBy: OrderBy): String {
    return when (orderBy) {
        OrderBy.NAME -> "title ASC"
        OrderBy.NAME_DESC -> "title DESC"
        OrderBy.TITLE -> "title ASC"
        OrderBy.TITLE_DESC -> "title DESC"
        OrderBy.MODIFIED -> "modified ASC"
        OrderBy.MODIFIED_DESC -> "modified DESC"
    }
}