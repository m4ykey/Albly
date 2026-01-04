package com.m4ykey.lyrics.di

import com.m4ykey.lyrics.presentation.LyricsViewModel
import org.koin.dsl.module

val lyricsViewModelModule = module {
    single { LyricsViewModel(get()) }
}