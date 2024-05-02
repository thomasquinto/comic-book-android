package com.quinto.comicbook.data.repository.dto

import com.quinto.comicbook.domain.model.Item

interface MappedItem {
    fun toItem(): Item
}