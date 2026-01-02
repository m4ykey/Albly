package com.m4ykey.albly.di

import com.m4ykey.albly.album.data.repository.AlbumRepositoryImpl
import com.m4ykey.albly.album.domain.repository.AlbumRepository
import com.m4ykey.albly.search.data.repository.SearchRepositoryImpl
import com.m4ykey.albly.search.domain.repository.SearchRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> { SearchRepositoryImpl(get()) }
    single<AlbumRepository> { AlbumRepositoryImpl(get(), get()) }
}