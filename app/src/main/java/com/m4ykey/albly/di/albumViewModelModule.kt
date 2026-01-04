package com.m4ykey.albly.di

import com.m4ykey.albly.album.presentation.detail.AlbumDetailViewModel
import com.m4ykey.albly.album.presentation.listen_later.ListenLaterViewModel
import com.m4ykey.albly.album.presentation.new_release.NewReleaseViewModel
import com.m4ykey.albly.collection.presentation.CollectionViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val albumViewModelModule = module {
    viewModel { AlbumDetailViewModel(
        getLocalAlbumUseCase = get(),
        useCase = get(),
        toggleAlbumSavedUseCase = get(),
        toggleListenLaterSavedUseCase = get(),
        getAlbumStateUseCase = get()) }
    viewModel { NewReleaseViewModel(get()) }
    viewModel { CollectionViewModel(get()) }
    viewModel { ListenLaterViewModel(get(), get()) }
}