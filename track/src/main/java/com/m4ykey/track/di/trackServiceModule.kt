package com.m4ykey.track.di

import com.m4ykey.track.data.network.service.RemoteTrackService
import com.m4ykey.track.data.network.service.TrackService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val trackServiceModule = module {
    single<RemoteTrackService> {
        TrackService(get(named("SpotifyHttpClient")))
    }
}