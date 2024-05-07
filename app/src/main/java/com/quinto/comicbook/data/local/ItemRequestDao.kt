package com.quinto.comicbook.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ItemRequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveItemRequest(
        itemRequest: ItemRequest
    )

    @Query("SELECT * FROM itemrequest WHERE paramKey = :paramKey AND paramExtras = :paramExtras")
    suspend fun retrieveItemRequest(paramKey: String, paramExtras: String): ItemRequest?

    @Query("DELETE FROM itemrequest WHERE paramKey = :paramKey")
    suspend fun clearItemRequestsForKey(paramKey: String)

    @Query("DELETE FROM itemrequest")
    suspend fun clearItemRequests()
}