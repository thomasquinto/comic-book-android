package com.quinto.comicbook.data.repository

import com.quinto.comicbook.data.local.ComicBookDatabase
import com.quinto.comicbook.data.local.ItemRequest
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
import java.util.Date
import javax.inject.Inject

class ComicBookRepositoryImpl @Inject constructor(
    private val api: ComicBookApi,
    private val db: ComicBookDatabase
) : ComicBookRepository {

    private val itemDao = db.itemDao
    private val itemRequestDao = db.itemRequestDao

    private suspend fun <T:  MappedItem> getItems(
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String,
        fetchFromRemote: Boolean,
        getRemoteItems: suspend (String, Long, String, Int, Int, String, String?) -> ResponseDto<T>,
        itemType: ItemType
    ): Flow<Resource<List<Item>>> {
        return flow {
            emit(Resource.Loading(true))

            if (fetchFromRemote) {
                itemRequestDao.clearItemRequestsForKey(ItemRequest.generateParamKey(itemType.typeName, null, null))
            }

            itemRequestDao.retrieveItemRequest(
                ItemRequest.generateParamKey(itemType.typeName, null, null),
                ItemRequest.generateParamExtras(startsWith, orderBy, offset, limit)
            )?.let { request ->
                println("Retrieved from local db: ${request.itemIds}")
                val itemList = itemDao.retrieveItems(request.itemIds).map { it.toItem() }
                emit(
                    Resource.Success(
                       data = itemList.sortedBy { request.itemIds.indexOf(it.id) } // retain order of items
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }

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

            remoteItems?.let { items ->

                items.map {
                    itemDao.saveItem(it.toEntity())
                }

                val itemIds = items.map { it.id }
                println("Saving to local db: $itemIds")

                itemRequestDao.saveItemRequest(
                    ItemRequest(
                        ItemRequest.generateParamKey(itemType.typeName, null, null),
                        ItemRequest.generateParamExtras(startsWith, orderBy, offset, limit),
                        itemIds,
                        Date()
                    )
                )

                emit(
                    Resource.Success(
                        data = items
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
        getRemoteDetails: suspend (String, Int, String, Long, String, Int, Int, String) -> ResponseDto<T>,
        itemType: ItemType
    ): Flow<Resource<List<Item>>> {
        return flow {
            emit(Resource.Loading(true))

            if (fetchFromRemote) {
                itemRequestDao.clearItemRequestsForKey(ItemRequest.generateParamKey(itemType.typeName, prefix, id))
            }

            itemRequestDao.retrieveItemRequest(
                ItemRequest.generateParamKey(itemType.typeName, prefix, id),
                ItemRequest.generateParamExtras("", orderBy, offset, limit)
            )?.let { request ->
                println("Retrieved from local db: ${request.itemIds}")
                val itemList = itemDao.retrieveItems(request.itemIds).map { it.toItem() }
                emit(
                    Resource.Success(
                        data = itemList.sortedBy { request.itemIds.indexOf(it.id) } // retain order of items
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }

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

            remoteItems?.let { items ->
                items.map {
                    itemDao.saveItem(it.toEntity())
                }

                val itemIds = items.map { it.id }
                println("Saving to local db: $itemIds")

                itemRequestDao.saveItemRequest(
                    ItemRequest(
                        ItemRequest.generateParamKey(itemType.typeName, prefix, id),
                        ItemRequest.generateParamExtras("", orderBy, offset, limit),
                        itemIds,
                        Date()
                    )
                )

                emit(
                    Resource.Success(
                        data = items
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
        startsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getItems(offset, limit, orderBy, startsWith, fetchFromRemote, api::getCharacters, ItemType.CHARACTER)
    }

    override suspend fun getComics(
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getItems(offset, limit, orderBy, startsWith, fetchFromRemote, api::getComics, ItemType.COMIC)
    }

    override suspend fun getCreators(
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getItems(offset, limit, orderBy, startsWith, fetchFromRemote, api::getCreators, ItemType.CREATOR)
    }

    override suspend fun getEvents(
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getItems(offset, limit, orderBy, startsWith, fetchFromRemote, api::getEvents, ItemType.EVENT)
    }

    override suspend fun getSeries(
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getItems(offset, limit, orderBy, startsWith, fetchFromRemote, api::getSeries, ItemType.SERIES)
    }

    override suspend fun getStories(
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getItems(offset, limit, orderBy, startsWith, fetchFromRemote, api::getStories, ItemType.STORY)
    }

    override suspend fun getCharacterDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getDetails(prefix, id, offset, limit, orderBy, fetchFromRemote, api::getCharacterDetails, ItemType.CHARACTER)
    }
    override suspend fun getComicDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getDetails(prefix, id, offset, limit, orderBy, fetchFromRemote, api::getComicDetails, ItemType.COMIC)
    }

    override suspend fun getCreatorDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getDetails(prefix, id, offset, limit, orderBy, fetchFromRemote, api::getCreatorDetails, ItemType.CREATOR)
    }

    override suspend fun getEventDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getDetails(prefix, id, offset, limit, orderBy, fetchFromRemote, api::getEventDetails, ItemType.EVENT)
    }

    override suspend fun getSeriesDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getDetails(prefix, id, offset, limit, orderBy, fetchFromRemote, api::getSeriesDetails, ItemType.SERIES)
    }

    override suspend fun getStoryDetails(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return getDetails(prefix, id, offset, limit, orderBy, fetchFromRemote, api::getStoryDetails, ItemType.STORY)
    }

    override suspend fun saveItem(item: Item) {
        itemDao.saveItem(item.toEntity())
    }

    override suspend fun retrieveItem(itemId: Int): Item {
        return itemDao.retrieveItem(itemId).toItem()
    }

    override suspend fun deleteCache() {
        itemDao.clearItems()
        itemRequestDao.clearItemRequests()
    }
}