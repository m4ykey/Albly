package com.m4ykey.track.di

import com.m4ykey.track.presentation.TrackViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val trackViewModelModule = module {
    viewModelOf(::TrackViewModel)
}