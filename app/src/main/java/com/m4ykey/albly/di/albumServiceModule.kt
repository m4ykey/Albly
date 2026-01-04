package com.m4ykey.albly.di

import com.m4ykey.albly.album.data.network.service.AlbumService
import com.m4ykey.albly.album.data.network.service.RemoteAlbumService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val albumServiceModule = module {
    single<RemoteAlbumService> {
        AlbumService(get(named("SpotifyHttpClient")))
    }
}