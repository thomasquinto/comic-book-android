package com.quinto.comicbook.domain.repository

import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.data.remote.OrderBy
import com.quinto.comicbook.util.Resource
import kotlinx.coroutines.flow.Flow

interface ComicBookRepository {

    suspend fun getCharacters(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.TITLE,
        nameStartsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>>

    suspend fun getComics(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.TITLE,
        titleStartsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>>

    suspend fun getCreators(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.TITLE,
        nameStartsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>>

    suspend fun getEvents(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.TITLE,
        nameStartsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>>

    suspend fun getSeries(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.TITLE,
        titleStartsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>>

    suspend fun getStories(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.TITLE,
        titleStartsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>>

}