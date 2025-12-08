package com.m4ykey.lyrics.di

import com.m4ykey.lyrics.data.repository.LyricsRepositoryImpl
import com.m4ykey.lyrics.domain.repository.LyricsRepository
import org.koin.dsl.module

val lyricsRepositoryModule = module {
    single<LyricsRepository> { LyricsRepositoryImpl(get()) }
}