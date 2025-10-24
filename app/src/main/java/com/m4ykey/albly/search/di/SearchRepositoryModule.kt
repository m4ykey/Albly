package com.m4ykey.albly.search.di

import com.m4ykey.albly.search.data.repository.RemoteSearchRepository
import com.m4ykey.albly.search.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchRepositoryModule {

    @Provides
    @Singleton
    fun provideSearchRepository(repository : RemoteSearchRepository) : SearchRepository = repository

}