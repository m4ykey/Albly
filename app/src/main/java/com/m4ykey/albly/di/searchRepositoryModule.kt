package com.m4ykey.albly.di

import com.m4ykey.albly.search.data.repository.SearchRepositoryImpl
import com.m4ykey.albly.search.domain.repository.SearchRepository
import org.koin.dsl.module

val searchRepositoryModule = module {
    single<SearchRepository> { SearchRepositoryImpl(get()) }

}