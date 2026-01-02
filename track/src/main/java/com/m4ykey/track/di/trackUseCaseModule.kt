package com.m4ykey.track.di

import com.m4ykey.track.domain.use_case.DeleteTracksUseCase
import com.m4ykey.track.domain.use_case.GetTrackUseCase
import com.m4ykey.track.domain.use_case.SaveTracksUseCase
import com.m4ykey.track.domain.use_case.TrackUseCase
import org.koin.dsl.module

val trackUseCaseModule = module {
    single { TrackUseCase(get()) }
    single { SaveTracksUseCase(get()) }
    single { GetTrackUseCase(get()) }
    single { DeleteTracksUseCase(get()) }
}