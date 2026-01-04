package com.m4ykey.lyrics.di

import org.koin.dsl.module

val lyricsModule = module {
    includes(
        lyricsApiModule,
        lyricsServiceModule,
        lyricsRepositoryModule,
        lyricsViewModelModule,
        lyricsUseCaseModule
    )
}