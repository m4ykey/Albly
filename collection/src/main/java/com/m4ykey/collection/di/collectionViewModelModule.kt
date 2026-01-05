package com.m4ykey.collection.di

import com.m4ykey.collection.presentation.CollectionViewModel
import org.koin.dsl.module

val collectionViewModel = module {
    single { CollectionViewModel(get()) }
}