package com.m4ykey.track.di

import com.m4ykey.track.presentation.TrackViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val trackViewModelModule = module {
    viewModel {
        TrackViewModel(
            useCase = get(),
            getTrackUseCase = get()
        )
    }
}