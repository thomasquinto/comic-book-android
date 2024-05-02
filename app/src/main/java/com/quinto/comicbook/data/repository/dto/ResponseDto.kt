package com.quinto.comicbook.data.repository.dto

data class ResponseDto<T>(
    val code: Int,
    val status: String,
    val data: DataDto<T>
)
