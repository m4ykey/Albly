package com.m4ykey.track.di

import com.m4ykey.track.data.repository.TrackRepositoryImpl
import com.m4ykey.track.domain.repository.TrackRepository
import org.koin.dsl.module

val trackRepositoryModule = module {
    single<TrackRepository> { TrackRepositoryImpl(get(), get()) }
}

