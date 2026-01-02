package com.m4ykey.albly.di

import com.m4ykey.albly.album.data.network.service.AlbumService
import com.m4ykey.albly.album.data.network.service.RemoteAlbumService
import com.m4ykey.albly.search.data.network.service.RemoteSearchService
import com.m4ykey.albly.search.data.network.service.SearchService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val serviceModule = module {
    single<RemoteSearchService> {
        SearchService(get(named("SpotifyHttpClient")))
    }
    single<RemoteAlbumService> {
        AlbumService(get(named("SpotifyHttpClient")))
    }
}