package com.m4ykey.lyrics.di

import com.m4ykey.lyrics.data.repository.LyricsRepositoryImpl
import com.m4ykey.lyrics.domain.repository.LyricsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val lyricsRepositoryModule = module {
    singleOf(::LyricsRepositoryImpl) bind LyricsRepository::class
}