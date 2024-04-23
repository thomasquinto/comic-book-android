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

    // Changed from this to be a "starts with" search
    //            WHERE LOWER(title) LIKE '%' || LOWER(:query) || '%'
    @Query(
        """
            SELECT * 
            FROM comicentity
            WHERE LOWER(title) LIKE LOWER(:query) || '%'
            ORDER BY title ASC
        """
    )
    suspend fun searchComics(query: String): List<ComicEntity>
}