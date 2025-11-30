package com.m4ykey.albly.di

import com.m4ykey.albly.album.presentation.detail.AlbumDetailViewModel
import com.m4ykey.albly.album.presentation.new_release.NewReleaseViewModel
import com.m4ykey.albly.search.presentation.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SearchViewModel(get()) }
    viewModel { AlbumDetailViewModel(get(), get()) }
    viewModel { NewReleaseViewModel(get()) }
}