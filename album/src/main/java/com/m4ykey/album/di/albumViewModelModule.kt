package com.m4ykey.album.di

import com.m4ykey.album.presentation.detail.AlbumDetailViewModel
import com.m4ykey.album.presentation.listen_later.ListenLaterViewModel
import com.m4ykey.album.presentation.new_release.NewReleaseViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val albumViewModelModule = module {
    viewModel {
        AlbumDetailViewModel(
            getLocalAlbumUseCase = get(),
            useCase = get(),
            toggleAlbumSavedUseCase = get(),
            toggleListenLaterSavedUseCase = get(),
            getAlbumStateUseCase = get()
        )
    }
    viewModel { NewReleaseViewModel(get()) }
    viewModel { ListenLaterViewModel(get(), get()) }
}