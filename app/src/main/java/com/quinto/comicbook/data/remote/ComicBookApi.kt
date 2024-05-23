package com.quinto.comicbook.data.remote

import com.quinto.comicbook.data.repository.dto.CharacterDto
import com.quinto.comicbook.data.repository.dto.ComicDto
import com.quinto.comicbook.data.repository.dto.CreatorDto
import com.quinto.comicbook.data.repository.dto.EventDto
import com.quinto.comicbook.data.repository.dto.ResponseDto
import com.quinto.comicbook.data.repository.dto.SeriesDto
import com.quinto.comicbook.data.repository.dto.StoryDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.security.MessageDigest

interface ComicBookApi {

    companion object {
        const val PUBLIC_KEY = "***REMOVED***"
        const val PRIVATE_KEY = "***REMOVED***"
        //const val BASE_URL = "https://gateway.marvel.com:443/v1/public/"
        const val BASE_URL = "http://3.101.90.31:8080/v1/public/"
        const val IMAGE_MISSING_URL = "https://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg"

        fun generateHash(ts: String): String {
            return "$ts$PRIVATE_KEY$PUBLIC_KEY".md5()
        }
    }

    @GET("characters")
    suspend fun getCharacters(
        @Query("apikey") apiKey: String = PUBLIC_KEY,
        @Query("ts") ts: Long = System.currentTimeMillis(),
        @Query("hash") hash: String = generateHash(ts.toString()),
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: String,
        @Query("nameStartsWith") nameStartsWith: String? = null
    ): ResponseDto<CharacterDto>

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

    @GET("creators")
    suspend fun getCreators(
        @Query("apikey") apiKey: String = PUBLIC_KEY,
        @Query("ts") ts: Long = System.currentTimeMillis(),
        @Query("hash") hash: String = generateHash(ts.toString()),
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: String,
        @Query("nameStartsWith") nameStartsWith: String? = null
    ): ResponseDto<CreatorDto>

    @GET("events")
    suspend fun getEvents(
        @Query("apikey") apiKey: String = PUBLIC_KEY,
        @Query("ts") ts: Long = System.currentTimeMillis(),
        @Query("hash") hash: String = generateHash(ts.toString()),
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: String,
        @Query("nameStartsWith") nameStartsWith: String? = null
    ): ResponseDto<EventDto>

    @GET("series")
    suspend fun getSeries(
        @Query("apikey") apiKey: String = PUBLIC_KEY,
        @Query("ts") ts: Long = System.currentTimeMillis(),
        @Query("hash") hash: String = generateHash(ts.toString()),
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: String,
        @Query("titleStartsWith") titleStartsWith: String? = null
    ): ResponseDto<SeriesDto>

    @GET("stories")
    suspend fun getStories(
        @Query("apikey") apiKey: String = PUBLIC_KEY,
        @Query("ts") ts: Long = System.currentTimeMillis(),
        @Query("hash") hash: String = generateHash(ts.toString()),
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: String,
        @Query("titleStartsWith") titleStartsWith: String? = null
    ): ResponseDto<StoryDto>

    @GET("{prefix}/{id}/characters")
    suspend fun getCharacterDetails(
        @Path("prefix") prefix: String,
        @Path("id") id: Int,
        @Query("apikey") apiKey: String = PUBLIC_KEY,
        @Query("ts") ts: Long = System.currentTimeMillis(),
        @Query("hash") hash: String = generateHash(ts.toString()),
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: String,
        @Query("nameStartsWith") nameStartsWith: String? = null
    ): ResponseDto<CharacterDto>

    @GET("{prefix}/{id}/comics")
    suspend fun getComicDetails(
        @Path("prefix") prefix: String,
        @Path("id") id: Int,
        @Query("apikey") apiKey: String = PUBLIC_KEY,
        @Query("ts") ts: Long = System.currentTimeMillis(),
        @Query("hash") hash: String = generateHash(ts.toString()),
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: String,
        @Query("titleStartsWith") titleStartsWith: String? = null
    ): ResponseDto<ComicDto>

    @GET("{prefix}/{id}/creators")
    suspend fun getCreatorDetails(
        @Path("prefix") prefix: String,
        @Path("id") id: Int,
        @Query("apikey") apiKey: String = PUBLIC_KEY,
        @Query("ts") ts: Long = System.currentTimeMillis(),
        @Query("hash") hash: String = generateHash(ts.toString()),
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: String,
        @Query("nameStartsWith") nameStartsWith: String? = null
    ): ResponseDto<CreatorDto>

    @GET("{prefix}/{id}/events")
    suspend fun getEventDetails(
        @Path("prefix") prefix: String,
        @Path("id") id: Int,
        @Query("apikey") apiKey: String = PUBLIC_KEY,
        @Query("ts") ts: Long = System.currentTimeMillis(),
        @Query("hash") hash: String = generateHash(ts.toString()),
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: String,
        @Query("nameStartsWith") nameStartsWith: String? = null
    ): ResponseDto<EventDto>

    @GET("{prefix}/{id}/series")
    suspend fun getSeriesDetails(
        @Path("prefix") prefix: String,
        @Path("id") id: Int,
        @Query("apikey") apiKey: String = PUBLIC_KEY,
        @Query("ts") ts: Long = System.currentTimeMillis(),
        @Query("hash") hash: String = generateHash(ts.toString()),
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: String,
        @Query("titleStartsWith") titleStartsWith: String? = null
    ): ResponseDto<SeriesDto>

    @GET("{prefix}/{id}/stories")
    suspend fun getStoryDetails(
        @Path("prefix") prefix: String,
        @Path("id") id: Int,
        @Query("apikey") apiKey: String = PUBLIC_KEY,
        @Query("ts") ts: Long = System.currentTimeMillis(),
        @Query("hash") hash: String = generateHash(ts.toString()),
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: String,
        @Query("titleStartsWith") titleStartsWith: String? = null
    ): ResponseDto<StoryDto>
}

private fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(toByteArray())
    return digest.joinToString("") {
        "%02x".format(it)
    }
}

enum class OrderBy(val value: String) {
    NAME("name"),
    NAME_DESC("-name"),
    FIRST_NAME("firstName"),
    FIRST_NAME_DESC("-firstName"),
    LAST_NAME("lastName"),
    LAST_NAME_DESC("-lastName"),
    TITLE("title"),
    TITLE_DESC("-title"),
    MODIFIED("modified"),
    MODIFIED_DESC("-modified"),
    ON_SALE_DATE("onsaleDate"),
    ON_SALE_DATE_DESC("-onsaleDate"),
    START_DATE("startDate"),
    START_DATE_DESC("-startDate"),
    START_YEAR("startYear"),
    START_YEAR_DESC("-startYear"),
}