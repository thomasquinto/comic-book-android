package com.quinto.comicbook.presentation.item_detail

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.quinto.comicbook.BuildConfig
import com.quinto.comicbook.data.remote.ComicBookApi
import com.quinto.comicbook.domain.model.Item
import com.quinto.comicbook.domain.model.ItemType
import com.quinto.comicbook.domain.repository.ComicBookRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel(assistedFactory = ItemDetailViewModel.ItemDetailViewModelFactory::class)
class ItemDetailViewModel @AssistedInject constructor(
    @Assisted private val item: Item,
    private val repository: ComicBookRepository
)  : ViewModel(){

    @AssistedFactory
    interface ItemDetailViewModelFactory {
        fun create(
            item: Item
        ): ItemDetailViewModel
    }

    var state by mutableStateOf(ItemDetailViewState())

    private var coroutine: Job? = null

    init {
        viewModelScope.launch {
            repository.retrieveFavoriteItems().let { favoriteItems ->
                state = state.copy(
                    isFavorite = favoriteItems.map { "$it.id-$it.itemType" }.contains("$item.id-$item.itemType")
                )
            }
        }
    }

    fun onEvent(event: ItemDetailViewEvent) {
        when (event) {
            is ItemDetailViewEvent.Favorite -> {
                state = state.copy(
                    isFavorite = !state.isFavorite
                )
                viewModelScope.launch {
                    repository.updateFavorite(event.item.id, event.item.itemType.typeName, state.isFavorite)
                }
            }
        }
    }

    fun onDetailImageLoaded(bitmap: Bitmap) {
        if (state.contentDescription.isNotEmpty()) {
            return
        }

        if (coroutine != null) {
            coroutine?.cancel()
        }

        coroutine = viewModelScope.launch {
            try {
                val generativeModel = GenerativeModel(
                    modelName = "gemini-1.5-flash",
                    apiKey = BuildConfig.GEMINI_API_KEY
                )

                val prompt = when(item.itemType) {
                    ItemType.CHARACTER -> "Please provide a description of the Marvel character named '${item.name}'. Some additional details are '${item.description}'."
                    ItemType.COMIC -> "This is a Marvel comic titled '${item.name}'. Some additional details are '${item.description}'. Please provide details of this comic."
                    ItemType.CREATOR -> "Please provide a description of the Marvel illustrator, artist or writer named '${item.name}'. Some additional details are '${item.description}'."
                    ItemType.EVENT -> "Please provide a description of the comic book event '${item.name}'. Some additional details are '${item.description}'."
                    ItemType.SERIES -> "Please provide a description of the Marvel comic series '${item.name}'"
                    else -> ""
                }

                if (prompt.isEmpty()) {
                    return@launch
                }

                val inputContent = content {
                    if (item.imageUrl != ComicBookApi.IMAGE_MISSING_URL) image(bitmap)
                    text(prompt)
                }
                val response = generativeModel.generateContent(inputContent)
                response.text?.let {
                    val text = response.text!!.replace("## ","").replace("**","").replace("* ", "• ")
                    state = state.copy(
                        contentDescription = text
                    )
                }
            } catch (e: CancellationException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun cancelCoroutine() {
        coroutine?.cancel()
    }
}