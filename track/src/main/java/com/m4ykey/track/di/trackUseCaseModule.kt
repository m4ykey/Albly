package com.m4ykey.track.di

import com.m4ykey.track.domain.use_case.DeleteTracksUseCase
import com.m4ykey.track.domain.use_case.GetTrackUseCase
import com.m4ykey.track.domain.use_case.SaveTracksUseCase
import com.m4ykey.track.domain.use_case.TrackUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val trackUseCaseModule = module {
    factoryOf(::TrackUseCase)
    factoryOf(::SaveTracksUseCase)
    factoryOf(::GetTrackUseCase)
    factoryOf(::DeleteTracksUseCase)
}