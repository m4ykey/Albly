package com.m4ykey.search.di

import com.m4ykey.search.data.repository.SearchRepositoryImpl
import com.m4ykey.search.domain.repository.SearchRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val searchRepositoryModule = module {
    singleOf(::SearchRepositoryImpl) bind SearchRepository::class
}