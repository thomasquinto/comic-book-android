package com.quinto.comicbook.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.quinto.comicbook.data.remote.OrderBy
import java.util.Date

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertItems(
        itemEntities: List<ItemEntity>
    )

    @Query("DELETE FROM itemEntity WHERE isFavorite != 1")
    suspend fun clearItems()

    // Changed from this to be a "starts with" search
    //            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%'
    @Query(
        """
            SELECT * 
            FROM itemEntity
            WHERE LOWER(name) LIKE LOWER(:query) || '%'
            ORDER BY (:orderBy)
        """
    )
    suspend fun searchItems(query: String, orderBy: String): List<ItemEntity>

    @Query(
        """
            SELECT * 
            FROM itemEntity
            WHERE LOWER(name) LIKE LOWER(:query) || '%'
            ORDER BY (:orderBy)
        """
    )
    suspend fun searchItemsBy(query: String, orderBy: String): List<ItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveItem(itemEntity: ItemEntity)

    @Query("SELECT * FROM itemEntity WHERE id = :itemId AND itemType = :itemType")
   suspend fun retrieveItem(itemId: Int, itemType: String): ItemEntity

   @Query("SELECT * FROM itemEntity WHERE id IN (:itemIds) AND itemType = :itemType")
    suspend fun retrieveItems(itemIds: List<Int>, itemType: String): List<ItemEntity>

    @Query("SELECT * FROM itemEntity WHERE isFavorite = 1 ORDER BY updated DESC")
    suspend fun retrieveFavoriteItems(): List<ItemEntity>

    @Query("UPDATE itemEntity SET isFavorite = :isFavorite, updated = :updated WHERE id = :itemId AND itemType = :itemType")
    suspend fun updateFavorite(itemId: Int, itemType: String, isFavorite: Boolean, updated: Date = Date())
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