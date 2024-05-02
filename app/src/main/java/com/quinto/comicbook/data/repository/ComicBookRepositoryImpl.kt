package com.quinto.comicbook.data.repository

import com.quinto.comicbook.data.local.ComicBookDatabase
import com.quinto.comicbook.data.mapper.toItem
import com.quinto.comicbook.data.remote.ComicBookApi
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.model.OrderBy
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
): ComicBookRepository {

    private val dao = db.dao

    override suspend fun getComics(
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        titleStartsWith: String,
        fetchFromRemote: Boolean
        ): Flow<Resource<List<Item>>> {
        return flow {
            emit(Resource.Loading(true))

/*
            val localItems = db.dao.searchItems(titleStartsWith, toDbOrderBy(orderBy))
            emit(Resource.Success(
                data = localComics.map { it.toItem() }
            ))

            val isDbEmpty = localItems.isEmpty() && titleStartsWith.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }
*/

            val remoteItems = try {
                val response = api.getComics(
                    offset = offset,
                    limit = limit,
                    orderBy = orderBy.value,
                    titleStartsWith = titleStartsWith.ifBlank { null }
                )
                response.data.results.map { it.toItem() }
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load comic data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load comic data"))
                null
            }

/*
            // If no remote items and db is not empty, just return the db items
            if (remoteItems.isNullOrEmpty() && !isDbEmpty) {
                emit(Resource.Loading(false))
                return@flow
            }

            remoteItems?.let { listings ->
                //dao.clearItems()
                dao.insertItems(
                    listings.map { it.toEntity() }
                )
                emit(Resource.Success(
                    data = dao
                        .searchItems(titleStartsWith, toDbOrderBy(orderBy))
                        .map { it.toItem() }
                ))
                emit(Resource.Loading(false))
            }
*/

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

}