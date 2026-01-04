package com.m4ykey.albly.di

import com.m4ykey.albly.album.data.repository.AlbumRepositoryImpl
import com.m4ykey.albly.album.domain.repository.AlbumRepository
import org.koin.dsl.module

val albumRepositoryModule = module {
    single<AlbumRepository> { AlbumRepositoryImpl(get(), get()) }
}