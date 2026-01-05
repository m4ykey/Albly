package com.m4ykey.search.di

import com.m4ykey.search.data.network.service.RemoteSearchService
import com.m4ykey.search.data.network.service.SearchService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val searchServiceModule = module {
    single<RemoteSearchService> {
        SearchService(get(named("SpotifyHttpClient")))
    }

}