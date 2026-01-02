package com.m4ykey.albly.di

import com.m4ykey.albly.album.presentation.detail.AlbumDetailViewModel
import com.m4ykey.albly.album.presentation.listen_later.ListenLaterViewModel
import com.m4ykey.albly.album.presentation.new_release.NewReleaseViewModel
import com.m4ykey.albly.collection.presentation.CollectionViewModel
import com.m4ykey.albly.search.presentation.SearchViewModel
import com.m4ykey.albly.track.presentation.TrackViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SearchViewModel(get()) }
    viewModel { AlbumDetailViewModel(
        getLocalAlbumUseCase = get(),
        useCase = get(),
        toggleAlbumSavedUseCase = get(),
        toggleListenLaterSavedUseCase = get(),
        getAlbumStateUseCase = get()) }
    viewModel { NewReleaseViewModel(get()) }
    viewModel { CollectionViewModel(get()) }
    viewModel { TrackViewModel(
        useCase = get(),
        getTrackUseCase = get()
    ) }
    viewModel { ListenLaterViewModel(get(), get()) }
}