package com.m4ykey.albly.di

import com.m4ykey.albly.album.data.network.service.AlbumService
import com.m4ykey.albly.album.data.network.service.RemoteAlbumService
import com.m4ykey.albly.search.data.network.service.RemoteSearchService
import com.m4ykey.albly.search.data.network.service.SearchService
import com.m4ykey.albly.track.data.network.service.RemoteTrackService
import com.m4ykey.albly.track.data.network.service.TrackService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val serviceModule = module {
    single<RemoteSearchService> {
        SearchService(get(named("SpotifyHttpClient")))
    }
    single<RemoteAlbumService> {
        AlbumService(get(named("SpotifyHttpClient")))
    }
    single<RemoteTrackService> {
        TrackService(get(named("SpotifyHttpClient")))
    }
}