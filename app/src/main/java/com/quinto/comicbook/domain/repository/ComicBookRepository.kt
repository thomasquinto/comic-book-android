package com.quinto.comicbook.domain.repository

import com.quinto.comicbook.domain.model.Comic
import com.quinto.comicbook.domain.model.OrderBy
import com.quinto.comicbook.util.Resource
import kotlinx.coroutines.flow.Flow

interface ComicBookRepository {

    suspend fun getComics(
        fetchFromRemote: Boolean,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        titleStartsWith: String = ""
    ): Flow<Resource<List<Comic>>>
}