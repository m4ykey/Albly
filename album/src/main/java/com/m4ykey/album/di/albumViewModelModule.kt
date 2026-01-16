package com.m4ykey.album.di

import com.m4ykey.album.presentation.detail.AlbumDetailViewModel
import com.m4ykey.album.presentation.listen_later.ListenLaterViewModel
import com.m4ykey.album.presentation.new_release.NewReleaseViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val albumViewModelModule = module {
    viewModelOf(::AlbumDetailViewModel)
    viewModelOf(::NewReleaseViewModel)
    viewModelOf(::ListenLaterViewModel)
}