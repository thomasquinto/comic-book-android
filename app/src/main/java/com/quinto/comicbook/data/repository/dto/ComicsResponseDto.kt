package com.quinto.comicbook.data.repository.dto

data class ComicsResponseDto(
    val code: Int,
    val status: String,
    val data: ComicsDataDto
)
