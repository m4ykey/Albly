package com.m4ykey.track.di

import org.koin.dsl.module

val trackModule = module {
    includes(trackRepositoryModule, trackServiceModule, trackUseCaseModule, trackViewModelModule)
}