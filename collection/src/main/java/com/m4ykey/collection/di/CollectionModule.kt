package com.m4ykey.collection.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.m4ykey.artist.domain.GetSavedArtistUseCase
import com.m4ykey.collection.preferences.CollectionPreferences
import com.m4ykey.collection.presentation.CollectionViewModel
import com.m4ykey.collection.repository.CollectionRepository
import com.m4ykey.collection.repository.CollectionRepositoryImpl
import com.m4ykey.core.datastore.appDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val collectionViewModel = module {
    viewModelOf(::CollectionViewModel)

    singleOf(::CollectionPreferences)

    single<DataStore<Preferences>> {
        androidContext().appDataStore
    }

    singleOf(::CollectionRepositoryImpl) bind CollectionRepository::class

    factoryOf(::GetSavedArtistUseCase)
}