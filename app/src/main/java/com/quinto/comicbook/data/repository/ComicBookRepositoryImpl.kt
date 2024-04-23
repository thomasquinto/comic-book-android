package com.quinto.comicbook.data.repository

import com.quinto.comicbook.data.local.ComicBookDatabase
import com.quinto.comicbook.data.mapper.toComic
import com.quinto.comicbook.data.mapper.toComicEntity
import com.quinto.comicbook.data.remote.ComicBookApi
import com.quinto.comicbook.domain.model.Comic
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
        fetchFromRemote: Boolean,
        offset: Int,
        limit: Int,
        orderBy: OrderBy,
        titleStartsWith: String
    ): Flow<Resource<List<Comic>>> {
        return flow {
            emit(Resource.Loading(true))
            val localComics = db.dao.searchComics(titleStartsWith)
            emit(Resource.Success(
                data = localComics.map { it.toComic() }
            ))

            val isDbEmpty = localComics.isEmpty() && titleStartsWith.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListings = try {
                val comicsResponse = api.getComics(
                    offset = offset,
                    limit = limit,
                    orderBy = orderBy.value,
                    titleStartsWith = titleStartsWith.ifBlank { null }
                )
                comicsResponse.data.results.map { it.toComic() }
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load comic data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load comic data"))
                null
            }

            // If no remote listings and db is not empty, just return the db listings
            if (remoteListings.isNullOrEmpty() && !isDbEmpty) {
                emit(Resource.Loading(false))
                return@flow
            }

            remoteListings?.let { listings ->
                //dao.clearComics()
                dao.insertComics(
                    listings.map { it.toComicEntity() }
                )
                emit(Resource.Success(
                    data = dao
                        .searchComics(titleStartsWith)
                        .map { it.toComic() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }

}