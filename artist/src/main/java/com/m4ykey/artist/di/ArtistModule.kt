package com.m4ykey.artist.di

import com.m4ykey.artist.data.repository.ArtistRepositoryImpl
import com.m4ykey.artist.domain.repository.ArtistRepository
import com.m4ykey.artist.domain.use_case.GetSavedArtistUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val artistModule = module {
    singleOf(::ArtistRepositoryImpl) bind ArtistRepository::class

    factoryOf(::GetSavedArtistUseCase)
}