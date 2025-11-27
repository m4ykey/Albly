package com.m4ykey.albly.di

import com.m4ykey.albly.album.domain.use_case.AlbumUseCase
import com.m4ykey.albly.search.domain.use_case.SearchUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { SearchUseCase(get()) }
    single { AlbumUseCase(get()) }
}