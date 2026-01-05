package com.m4ykey.search.di

import com.m4ykey.search.domain.use_case.SearchUseCase
import org.koin.dsl.module

val searchUseCaseModule = module {
    single { SearchUseCase(get()) }
}