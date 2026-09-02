package com.m4ykey.lyrics.di

import com.m4ykey.lyrics.data.repository.LyricsRepositoryImpl
import com.m4ykey.lyrics.domain.repository.LyricsRepository
import com.m4ykey.lyrics.domain.usecase.GetLyricsUseCase
import com.m4ykey.lyrics.presentation.LyricsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val lyricsModule = module {
    singleOf(::LyricsRepositoryImpl) bind LyricsRepository::class

    factoryOf(::GetLyricsUseCase)

    viewModelOf(::LyricsViewModel)
}