package com.quinto.comicbook.data.repository

import com.quinto.comicbook.data.local.ComicBookDatabase
import com.quinto.comicbook.data.mapper.toComic
import com.quinto.comicbook.data.mapper.toComicEntity
import com.quinto.comicbook.data.remote.ComicBookApi
import com.quinto.comicbook.domain.model.Comic
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
        query: String
    ): Flow<Resource<List<Comic>>> {
        return flow {
            emit(Resource.Loading(true))
            val localComics = db.dao.searchComics(query)
            emit(Resource.Success(
                data = localComics.map { it.toComic() }
            ))

            val isDbEmpty = localComics.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListings = try {
                val comicsResponse = api.getComics()
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

            remoteListings?.let { listings ->
                dao.clearComics()
                dao.insertComics(
                    listings.map { it.toComicEntity() }
                )
                emit(Resource.Success(
                    data = dao
                        .searchComics("")
                        .map { it.toComic() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }

}