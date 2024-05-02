package com.quinto.comicbook.presentation.home

import androidx.lifecycle.ViewModel
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.model.OrderBy
import com.quinto.comicbook.domain.repository.ComicBookRepository
import com.quinto.comicbook.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ComicBookRepository
): ViewModel() {

    suspend fun getComics(
        offset: Int,
        limit: Int,
        orderBy: OrderBy = OrderBy.TITLE,
        titleStartsWith: String = "",
        fetchFromRemote: Boolean = false
    ): Flow<Resource<List<Item>>> {
        return repository.getComics(offset, limit, orderBy, titleStartsWith, fetchFromRemote)
    }
}