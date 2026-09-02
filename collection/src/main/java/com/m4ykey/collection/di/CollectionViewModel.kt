package com.m4ykey.collection.di

import com.m4ykey.collection.presentation.CollectionViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val collectionViewModel = module {
    viewModelOf(::CollectionViewModel)
}