package com.quinto.comicbook.data.repository.dto

data class DataDto<T>(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<T>
)