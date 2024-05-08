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
        itemEntities: List<ItemEntity>
    )

    @Query("DELETE FROM itementity")
    suspend fun clearItems()

    // Changed from this to be a "starts with" search
    //            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%'
    @Query(
        """
            SELECT * 
            FROM itementity
            WHERE LOWER(name) LIKE LOWER(:query) || '%'
            ORDER BY (:orderBy)
        """
    )
    suspend fun searchItems(query: String, orderBy: String): List<ItemEntity>

    @Query(
        """
            SELECT * 
            FROM itementity
            WHERE LOWER(name) LIKE LOWER(:query) || '%'
            ORDER BY (:orderBy)
        """
    )
    suspend fun searchItemsBy(query: String, orderBy: String): List<ItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveItem(
        itemEntity: ItemEntity
    )

    @Query("SELECT * FROM itementity WHERE id = :itemId")
   suspend fun retrieveItem(itemId: Int): ItemEntity

   @Query("SELECT * FROM itementity WHERE id IN (:itemIds)")
    suspend fun retrieveItems(itemIds: List<Int>): List<ItemEntity>
}

fun toDbOrderBy(orderBy: OrderBy): String {
    return when (orderBy) {
        OrderBy.NAME -> "name ASC"
        OrderBy.NAME_DESC -> "name DESC"
        OrderBy.LAST_NAME -> "name ASC"
        OrderBy.LAST_NAME_DESC -> "name DESC"
        OrderBy.TITLE -> "name ASC"
        OrderBy.TITLE_DESC -> "name DESC"
        OrderBy.MODIFIED -> "date ASC"
        OrderBy.MODIFIED_DESC -> "date DESC"
        OrderBy.FIRST_NAME -> "name ASC"
        OrderBy.FIRST_NAME_DESC -> "name DESC"
        OrderBy.ON_SALE_DATE -> "date ASC"
        OrderBy.ON_SALE_DATE_DESC -> "date DESC"
        OrderBy.START_DATE -> "date ASC"
        OrderBy.START_DATE_DESC -> "date DESC"
        OrderBy.START_YEAR ->  "date ASC"
        OrderBy.START_YEAR_DESC -> "date DESC"
    }
}