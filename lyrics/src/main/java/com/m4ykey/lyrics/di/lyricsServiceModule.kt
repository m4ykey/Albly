package com.m4ykey.lyrics.di

import com.m4ykey.lyrics.data.network.service.LyricsService
import com.m4ykey.lyrics.data.network.service.RemoteLyricsService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val lyricsServiceModule = module {
    single<RemoteLyricsService> {
        LyricsService(get(named("LrclibHttpClient")))
    }
}