package com.m4ykey.lyrics.di

import com.m4ykey.lyrics.presentation.LyricsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val lyricsViewModelModule = module {
    singleOf(::LyricsViewModel)
}