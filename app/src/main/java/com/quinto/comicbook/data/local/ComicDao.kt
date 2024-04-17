package com.quinto.comicbook.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ComicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComics(
        comicEntities: List<ComicEntity>
    )

    @Query("DELETE FROM comicentity")
    suspend fun clearComics()

    @Query(
        """
            SELECT * 
            FROM comicentity
            WHERE LOWER(title) LIKE '%' || LOWER(:query) || '%'
        """
    )
    suspend fun searchComics(query: String): List<ComicEntity>
}