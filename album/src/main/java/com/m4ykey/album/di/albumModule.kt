package com.m4ykey.album.di

import org.koin.dsl.module

val albumModule = module {
    includes(
        albumRepositoryModule,
        albumServiceModule,
        albumUseCaseModule,
        albumViewModelModule
    )
}