package com.m4ykey.album.di

import com.m4ykey.album.data.repository.AlbumRepositoryImpl
import com.m4ykey.album.domain.repository.AlbumRepository
import com.m4ykey.album.domain.usecase.AlbumUseCase
import com.m4ykey.album.domain.usecase.GetAlbumStateUseCase
import com.m4ykey.album.domain.usecase.GetListenLaterAlbumsUseCase
import com.m4ykey.album.domain.usecase.GetLocalAlbumUseCase
import com.m4ykey.album.domain.usecase.GetRandomAlbumUseCase
import com.m4ykey.album.domain.usecase.GetSavedAlbumsUseCase
import com.m4ykey.album.domain.usecase.NewReleaseUseCase
import com.m4ykey.album.domain.usecase.ToggleAlbumSavedUseCase
import com.m4ykey.album.domain.usecase.ToggleListenLaterSavedUseCase
import com.m4ykey.album.presentation.detail.AlbumDetailViewModel
import com.m4ykey.album.presentation.listen_later.ListenLaterViewModel
import com.m4ykey.album.presentation.new_release.NewReleaseViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val albumModule = module {
    singleOf(::AlbumRepositoryImpl) bind AlbumRepository::class

    factoryOf(::AlbumUseCase)
    factoryOf(::GetAlbumStateUseCase)
    factoryOf(::GetListenLaterAlbumsUseCase)
    factoryOf(::GetRandomAlbumUseCase)
    factoryOf(::GetSavedAlbumsUseCase)
    factoryOf(::ToggleAlbumSavedUseCase)
    factoryOf(::ToggleListenLaterSavedUseCase)
    factoryOf(::GetLocalAlbumUseCase)
    factoryOf(::NewReleaseUseCase)

    viewModelOf(::AlbumDetailViewModel)
    viewModelOf(::ListenLaterViewModel)
    viewModelOf(::NewReleaseViewModel)
}