package com.m4ykey.album.di

import com.m4ykey.album.data.repository.AlbumRepositoryImpl
import com.m4ykey.album.domain.repository.AlbumRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val albumRepositoryModule = module {
    singleOf(::AlbumRepositoryImpl) bind AlbumRepository::class
}