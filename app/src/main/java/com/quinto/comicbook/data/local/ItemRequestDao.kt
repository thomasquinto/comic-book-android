package com.quinto.comicbook.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ItemRequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveItemRequest(
        itemRequestEntity: ItemRequestEntity
    )

    @Query("SELECT * FROM itemRequestEntity WHERE paramKey = :paramKey AND paramExtras = :paramExtras")
    suspend fun retrieveItemRequest(paramKey: String, paramExtras: String): ItemRequestEntity?

    @Query("DELETE FROM itemRequestEntity WHERE paramKey = :paramKey")
    suspend fun clearItemRequestsForKey(paramKey: String)

    @Query("DELETE FROM itemRequestEntity")
    suspend fun clearItemRequests()
}