package com.m4ykey.search.di

import org.koin.dsl.module

val searchModule = module {
    includes(searchRepositoryModule, searchServiceModule, searchUseCaseModule, searchViewModelModule)
}