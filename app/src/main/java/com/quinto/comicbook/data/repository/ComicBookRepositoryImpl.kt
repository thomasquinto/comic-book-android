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

    private suspend fun <T:  MappedItem> fetchItems(
        itemType: ItemType,
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String,
        fetchFromRemote: Boolean,
        fetchRemoteItems: (suspend (String, Long, String, Int, Int, String, String?) -> ResponseDto<T>)?,
        fetchRemoteDetails: (suspend (String, Int, String, Long, String, Int, Int, String, String?) -> ResponseDto<T>)?,
        ): Flow<Resource<List<Item>>> {
        return flow {
            emit(Resource.Loading(true))

            if (fetchFromRemote) {
                itemRequestDao.clearItemRequestsForKey(ItemRequest.generateParamKey(itemType.typeName, prefix, id))
            }

            itemRequestDao.retrieveItemRequest(
                ItemRequest.generateParamKey(itemType.typeName, prefix, id),
                ItemRequest.generateParamExtras(startsWith, orderBy, offset, limit)
            )?.let { request ->
                println("Retrieved from local db: ${request.itemIds}")
                val itemList = itemDao.retrieveItems(request.itemIds, itemType.typeName).map { it.toItem() }
                emit(
                    Resource.Success(
                        data = itemList.sortedBy { request.itemIds.indexOf(it.id) } // retain order of items
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteItems = if (fetchRemoteItems != null) {
                try {
                    val ts = System.currentTimeMillis()
                    val response = fetchRemoteItems(
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
            } else {
                try {
                    val ts = System.currentTimeMillis()
                    val response = fetchRemoteDetails!!(
                        prefix,
                        id,
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
            }

            remoteItems?.let { items ->

                items.map {
                    if (itemDao.retrieveItem(it.id, it.itemType.typeName) == null) {
                        itemDao.saveItem(it.toEntity())
                    }
                }

                val itemIds = items.map { it.id }
                println("Saving to local db: $itemIds")

                itemRequestDao.saveItemRequest(
                    ItemRequest(
                        ItemRequest.generateParamKey(itemType.typeName, prefix, id),
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

    override suspend fun getCharacters(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return if (id == 0) {
            fetchItems(ItemType.CHARACTER, prefix, id, offset, limit, orderBy, startsWith, fetchFromRemote, api::getCharacters, null)
        } else {
            fetchItems(ItemType.CHARACTER, prefix, id, offset, limit, orderBy, startsWith, fetchFromRemote, null, api::getCharacterDetails)
        }
    }

    override suspend fun getComics(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return if (id == 0) {
            fetchItems(ItemType.COMIC, prefix, id, offset, limit, orderBy, startsWith, fetchFromRemote, api::getComics, null)
        } else {
            fetchItems(ItemType.COMIC, prefix, id, offset, limit, orderBy, startsWith, fetchFromRemote, null, api::getComicDetails)
        }
    }

    override suspend fun getCreators(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return if (id == 0) {
            fetchItems(ItemType.CREATOR, prefix, id, offset, limit, orderBy, startsWith, fetchFromRemote, api::getCreators, null)
        } else {
            fetchItems(ItemType.CREATOR, prefix, id, offset, limit, orderBy, startsWith, fetchFromRemote, null, api::getCreatorDetails)
        }
    }

    override suspend fun getEvents(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return if (id == 0) {
            fetchItems(ItemType.EVENT, prefix, id, offset, limit, orderBy, startsWith, fetchFromRemote, api::getEvents, null)
        } else {
            fetchItems(ItemType.EVENT, prefix, id, offset, limit, orderBy, startsWith, fetchFromRemote, null, api::getEventDetails)
        }
    }

    override suspend fun getSeries(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return if (id == 0) {
            fetchItems(ItemType.SERIES, prefix, id, offset, limit, orderBy, startsWith, fetchFromRemote, api::getSeries, null)
        } else {
            fetchItems(ItemType.SERIES, prefix, id, offset, limit, orderBy, startsWith, fetchFromRemote, null, api::getSeriesDetails)
        }
    }

    override suspend fun getStories(
        prefix: String,
        id: Int,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        startsWith: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Item>>> {
        return if (id == 0) {
            fetchItems(ItemType.STORY, prefix, id, offset, limit, orderBy, startsWith, fetchFromRemote, api::getStories, null)
        } else {
            fetchItems(ItemType.STORY, prefix, id, offset, limit, orderBy, startsWith, fetchFromRemote, null, api::getStoryDetails)
        }
    }

    override suspend fun saveItem(item: Item) {
        itemDao.saveItem(item.toEntity())
    }

    override suspend fun retrieveItem(itemId: Int, itemType: String): Item {
        return itemDao.retrieveItem(itemId, itemType).toItem()
    }

    override suspend fun deleteCache() {
        itemDao.clearItems()
        itemRequestDao.clearItemRequests()
    }

    override suspend fun retrieveFavoriteItems(): List<Item> {
        return itemDao.retrieveFavoriteItems().map { it.toItem() }
    }

    override suspend fun updateFavorite(itemId: Int, itemType: String, isFavorite: Boolean) {
        itemDao.updateFavorite(itemId, itemType, isFavorite)
    }
}