package com.m4ykey.albly.app.di

import com.m4ykey.albly.app.ui.MainActivityViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainActivityViewModel)
}