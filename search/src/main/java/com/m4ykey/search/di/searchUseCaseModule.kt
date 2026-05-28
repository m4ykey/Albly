package com.m4ykey.search.di

import com.m4ykey.search.domain.use_case.SearchAlbumUseCase
import com.m4ykey.search.domain.use_case.SearchArtistUseCase
import com.m4ykey.search.domain.use_case.SearchLyricsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val searchUseCaseModule = module {
    factoryOf(::SearchAlbumUseCase)
    factoryOf(::SearchArtistUseCase)
    factoryOf(::SearchLyricsUseCase)
}