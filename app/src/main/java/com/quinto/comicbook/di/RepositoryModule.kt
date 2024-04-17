package com.quinto.comicbook.di

import com.quinto.comicbook.data.repository.ComicBookRepositoryImpl
import com.quinto.comicbook.domain.repository.ComicBookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindComicBookRepository(
        comicBookRepositoryImpl: ComicBookRepositoryImpl
    ): ComicBookRepository
}