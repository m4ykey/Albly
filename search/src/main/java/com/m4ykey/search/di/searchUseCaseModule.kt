package com.m4ykey.search.di

import com.m4ykey.search.domain.use_case.SearchAlbumUseCase
import com.m4ykey.search.domain.use_case.SearchArtistUseCase
import org.koin.dsl.module

val searchUseCaseModule = module {
    single { SearchAlbumUseCase(get()) }
    single { SearchArtistUseCase(get()) }
}