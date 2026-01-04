package com.m4ykey.albly.di

import com.m4ykey.albly.search.domain.use_case.SearchUseCase
import org.koin.dsl.module

val searchUseCaseModule = module {
    single { SearchUseCase(get()) }
}