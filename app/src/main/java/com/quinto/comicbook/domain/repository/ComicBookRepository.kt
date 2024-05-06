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
        startsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>>

    suspend fun getComics(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.TITLE,
        startsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>>

    suspend fun getCreators(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.TITLE,
        startsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>>

    suspend fun getEvents(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.TITLE,
        startsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>>

    suspend fun getSeries(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.TITLE,
        startsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>>

    suspend fun getStories(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.TITLE,
        startsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>>

    suspend fun getCharacterDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>>

    suspend fun getComicDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>>

    suspend fun getCreatorDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>>

    suspend fun getEventDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>>

    suspend fun getSeriesDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>>

    suspend fun getStoryDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>>

    suspend fun saveItem(item: Item)

    suspend fun retrieveItem(itemId: Int): Item
}