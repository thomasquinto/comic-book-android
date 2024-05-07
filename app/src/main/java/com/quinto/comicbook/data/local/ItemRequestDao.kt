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

    @Query("SELECT * FROM itemrequest WHERE paramKey = :params AND offsetParam = :offsetParam AND limitParam = :limitParam")
    suspend fun retrieveItemRequest(params: String, offsetParam: Int, limitParam: Int): ItemRequest?

    @Query("DELETE FROM itemrequest WHERE paramKey = :paramKey")
    suspend fun clearItemRequests(paramKey: String)
}