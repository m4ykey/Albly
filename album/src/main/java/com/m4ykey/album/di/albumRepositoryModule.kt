package com.m4ykey.album.di

import com.m4ykey.album.data.repository.AlbumRepositoryImpl
import com.m4ykey.album.domain.repository.AlbumRepository
import org.koin.dsl.module

val albumRepositoryModule = module {
    single<AlbumRepository> { AlbumRepositoryImpl(get(), get()) }
}