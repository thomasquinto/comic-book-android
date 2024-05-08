package com.quinto.comicbook.domain.repository

import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.data.remote.OrderBy
import com.quinto.comicbook.util.Resource
import kotlinx.coroutines.flow.Flow

interface ComicBookRepository {

    suspend fun getCharacters(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String = "",
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>>

    suspend fun getComics(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String = "",
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>>

    suspend fun getCreators(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String = "",
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>>

    suspend fun getEvents(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String = "",
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>>

    suspend fun getSeries(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String = "",
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>>

    suspend fun getStories(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String = "",
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>>

    suspend fun saveItem(item: Item)

    suspend fun retrieveItem(itemId: Int): Item

    suspend fun deleteCache()
}