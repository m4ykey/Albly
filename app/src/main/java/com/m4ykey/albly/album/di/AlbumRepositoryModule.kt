package com.m4ykey.albly.album.di

import com.m4ykey.albly.album.data.repository.AlbumRepositoryImpl
import com.m4ykey.albly.album.domain.repository.AlbumRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlbumRepositoryModule {

    @Provides
    @Singleton
    fun provideAlbumRepository(impl : AlbumRepositoryImpl) : AlbumRepository = impl

}