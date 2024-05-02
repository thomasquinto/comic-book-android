package com.quinto.comicbook.data.remote

import com.quinto.comicbook.data.repository.dto.ComicDto
import com.quinto.comicbook.data.repository.dto.ResponseDto
import retrofit2.http.GET
import retrofit2.http.Query
import java.security.MessageDigest

interface ComicBookApi {

    companion object {
        const val PUBLIC_KEY = "***REMOVED***"
        const val PRIVATE_KEY = "***REMOVED***"
        const val BASE_URL = "https://gateway.marvel.com:443/v1/public/"

        fun generateHash(ts: String): String {
            return "$ts$PRIVATE_KEY$PUBLIC_KEY".md5()
        }
    }

    // Example
    // http://gateway.marvel.com/v1/public/comics?ts=1&apikey=1234&hash=ffd275c5130566a2916217b101f26150
    @GET("comics")
    suspend fun getComics(
        @Query("apikey") apiKey: String = PUBLIC_KEY,
        @Query("ts") ts: Long = System.currentTimeMillis(),
        @Query("hash") hash: String = generateHash(ts.toString()),
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: String,
        @Query("titleStartsWith") titleStartsWith: String? = null
    ): ResponseDto<ComicDto>
}

private fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(toByteArray())
    return digest.joinToString("") {
        "%02x".format(it)
    }
}
