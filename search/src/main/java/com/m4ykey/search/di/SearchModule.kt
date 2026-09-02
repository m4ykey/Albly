package com.m4ykey.search.di

import com.m4ykey.search.data.repository.SearchRepositoryImpl
import com.m4ykey.search.domain.repository.SearchRepository
import com.m4ykey.search.domain.usecase.SearchAlbumUseCase
import com.m4ykey.search.domain.usecase.SearchArtistUseCase
import com.m4ykey.search.domain.usecase.SearchLyricsUseCase
import com.m4ykey.search.presentation.SearchViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val searchModule = module {
    singleOf(::SearchRepositoryImpl) bind SearchRepository::class

    factoryOf(::SearchAlbumUseCase)
    factoryOf(::SearchArtistUseCase)
    factoryOf(::SearchLyricsUseCase)

    viewModelOf(::SearchViewModel)
}