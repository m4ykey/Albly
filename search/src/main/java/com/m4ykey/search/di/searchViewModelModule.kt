package com.m4ykey.search.di

import com.m4ykey.search.presentation.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModel { SearchViewModel(get()) }
}