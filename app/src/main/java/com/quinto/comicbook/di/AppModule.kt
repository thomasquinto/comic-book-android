package com.quinto.comicbook.di

import android.app.Application
import androidx.room.Room
import com.quinto.comicbook.data.local.ComicBookDatabase
import com.quinto.comicbook.data.remote.ComicBookApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideComicBookApi(): ComicBookApi {
        val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

        return Retrofit.Builder()
            .baseUrl(ComicBookApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(
                OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }).build())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideComicBookDatabase(app: Application): ComicBookDatabase {
        return Room.databaseBuilder(
            app,
            ComicBookDatabase::class.java,
            "comicbook.db"
        ).build()
    }
}