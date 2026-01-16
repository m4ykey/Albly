package com.m4ykey.lyrics.di

import com.m4ykey.lyrics.domain.use_case.GetLyricsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val lyricsUseCaseModule = module {
    factoryOf(::GetLyricsUseCase)
}