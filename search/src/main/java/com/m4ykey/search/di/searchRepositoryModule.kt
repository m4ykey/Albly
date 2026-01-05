package com.m4ykey.search.di

import com.m4ykey.search.data.repository.SearchRepositoryImpl
import com.m4ykey.search.domain.repository.SearchRepository
import org.koin.dsl.module

val searchRepositoryModule = module {
    single<SearchRepository> { SearchRepositoryImpl(get()) }

}