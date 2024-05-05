package com.quinto.comicbook.data.repository

import com.quinto.comicbook.data.local.ComicBookDatabase
import com.quinto.comicbook.data.mapper.toEntity
import com.quinto.comicbook.data.mapper.toItem
import com.quinto.comicbook.data.remote.ComicBookApi
import com.quinto.comicbook.data.remote.OrderBy
import com.quinto.comicbook.data.repository.dto.MappedItem
import com.quinto.comicbook.data.repository.dto.ResponseDto
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.model.ItemType
import com.quinto.comicbook.domain.repository.ComicBookRepository
import com.quinto.comicbook.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ComicBookRepositoryImpl @Inject constructor(
    private val api: ComicBookApi,
    private val db: ComicBookDatabase
) : ComicBookRepository {

    private val dao = db.dao

    private suspend fun <T:  MappedItem> getItems(
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String,
        fetchFromRemote: Boolean,
        getRemoteItems: suspend (String, Long, String, Int, Int, String, String?) -> ResponseDto<T>
    ): Flow<Resource<List<Item>>> {
        return flow {
            emit(Resource.Loading(true))

            val remoteItems = try {
                val ts = System.currentTimeMillis()
                val response = getRemoteItems(
                    ComicBookApi.PUBLIC_KEY,
                    ts,
                    ComicBookApi.generateHash(ts.toString()),
                    offset,
                    limit,
                    orderBy.value,
                    startsWith.ifBlank { null }
                )
                response.data.results.map { it.toItem() }
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load response data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load response data"))
                null
            }

            remoteItems?.let { listings ->
                emit(
                    Resource.Success(
                        data = listings
                    )
                )
                emit(Resource.Loading(false))
            }
        }
    }

    private suspend fun <T:  MappedItem> getDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean,
        getRemoteDetails: suspend (String, Int, String, Long, String, Int, Int, String) -> ResponseDto<T>
    ): Flow<Resource<List<Item>>> {
        return flow {
            emit(Resource.Loading(true))

            val remoteItems = try {
                val ts = System.currentTimeMillis()
                val response = getRemoteDetails(
                    prefix,
                    id,
                    ComicBookApi.PUBLIC_KEY,
                    ts,
                    ComicBookApi.generateHash(ts.toString()),
                    offset,
                    limit,
                    orderBy.value,
                    )
                response.data.results.map { it.toItem() }
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load response data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load response data"))
                null
            }

            remoteItems?.let { listings ->
                emit(
                    Resource.Success(
                        data = listings
                    )
                )
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getCharacters(
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        nameStartsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getItems(offset, limit, orderBy, nameStartsWith, fetchFromRemote, api::getCharacters)
    }

    override suspend fun getComics(
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        titleStartsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getItems(offset, limit, orderBy, titleStartsWith, fetchFromRemote, api::getComics)
    }

    override suspend fun getCreators(
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        nameStartsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getItems(offset, limit, orderBy, nameStartsWith, fetchFromRemote, api::getCreators)
    }

    override suspend fun getEvents(
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        titleStartsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getItems(offset, limit, orderBy, titleStartsWith, fetchFromRemote, api::getEvents)
    }

    override suspend fun getSeries(
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        titleStartsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getItems(offset, limit, orderBy, titleStartsWith, fetchFromRemote, api::getSeries)
    }

    override suspend fun getStories(
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        titleStartsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getItems(offset, limit, orderBy, titleStartsWith, fetchFromRemote, api::getStories)
    }

    override suspend fun getCharacterDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getDetails(prefix, id, offset, limit, orderBy, fetchFromRemote, api::getCharacterDetails)
    }
    override suspend fun getComicDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getDetails(prefix, id, offset, limit, orderBy, fetchFromRemote, api::getComicDetails)
    }

    override suspend fun getCreatorDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getDetails(prefix, id, offset, limit, orderBy, fetchFromRemote, api::getCreatorDetails)
    }

    override suspend fun getEventDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getDetails(prefix, id, offset, limit, orderBy, fetchFromRemote, api::getEventDetails)
    }

    override suspend fun getSeriesDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getDetails(prefix, id, offset, limit, orderBy, fetchFromRemote, api::getSeriesDetails)
    }

    override suspend fun getStoryDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getDetails(prefix, id, offset, limit, orderBy, fetchFromRemote, api::getStoryDetails)
    }

    override suspend fun saveItem(item: Item) {
        dao.saveItem(item.toEntity())
    }

    override suspend fun retrieveItem(itemId: Int): Item {
        return dao.retrieveItem(itemId).toItem(ItemType.COMIC)
    }
}