package com.m4ykey.albly.di

import com.m4ykey.albly.album.domain.use_case.AlbumUseCase
import com.m4ykey.albly.album.domain.use_case.NewReleaseUseCase
import com.m4ykey.albly.search.domain.use_case.SearchUseCase
import com.m4ykey.albly.track.domain.use_case.TrackUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { SearchUseCase(get()) }
    single { AlbumUseCase(get()) }
    single { NewReleaseUseCase(get()) }
    single { TrackUseCase(get()) }
}