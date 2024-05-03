package com.quinto.comicbook.presentation.home

import androidx.lifecycle.ViewModel
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.data.remote.OrderBy
import com.quinto.comicbook.domain.repository.ComicBookRepository
import com.quinto.comicbook.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ComicBookRepository
): ViewModel() {

    suspend fun getCharacters(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.NAME,
        nameStartsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>> {
        return repository.getCharacters(offset, limit, orderBy, nameStartsWith, fetchFromRemote)
    }

    suspend fun getComics(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.TITLE,
        titleStartsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>> {
        return repository.getComics(offset, limit, orderBy, titleStartsWith, fetchFromRemote)
    }

    suspend fun getCreators(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.NAME,
        nameStartsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>> {
        return repository.getCreators(offset, limit, orderBy, nameStartsWith, fetchFromRemote)
    }

    suspend fun getEvents(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.TITLE,
        titleStartsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>> {
        return repository.getEvents(offset, limit, orderBy, titleStartsWith, fetchFromRemote)
    }

    suspend fun getSeries(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.TITLE,
        titleStartsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>> {
        return repository.getSeries(offset, limit, orderBy, titleStartsWith, fetchFromRemote)
    }

    suspend fun getStories(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.TITLE,
        titleStartsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>> {
        return repository.getStories(offset, limit, orderBy, titleStartsWith, fetchFromRemote)
    }

}