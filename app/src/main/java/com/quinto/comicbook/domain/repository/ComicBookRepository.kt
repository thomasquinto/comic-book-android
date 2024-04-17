package com.quinto.comicbook.domain.repository

import com.quinto.comicbook.domain.model.Comic
import com.quinto.comicbook.util.Resource
import kotlinx.coroutines.flow.Flow

interface ComicBookRepository {

    suspend fun getComics(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<Comic>>>
}