package com.m4ykey.album.di

import com.m4ykey.album.domain.use_case.AlbumUseCase
import com.m4ykey.album.domain.use_case.GetAlbumStateUseCase
import com.m4ykey.album.domain.use_case.GetListenLaterAlbumsUseCase
import com.m4ykey.album.domain.use_case.GetLocalAlbumUseCase
import com.m4ykey.album.domain.use_case.GetRandomAlbumUseCase
import com.m4ykey.album.domain.use_case.GetSavedAlbumsUseCase
import com.m4ykey.album.domain.use_case.NewReleaseUseCase
import com.m4ykey.album.domain.use_case.ToggleAlbumSavedUseCase
import com.m4ykey.album.domain.use_case.ToggleListenLaterSavedUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val albumUseCaseModule = module {
    factoryOf(::AlbumUseCase)
    factoryOf(::NewReleaseUseCase)
    factoryOf(::GetAlbumStateUseCase)
    factoryOf(::GetListenLaterAlbumsUseCase)
    factoryOf(::GetRandomAlbumUseCase)
    factoryOf(::GetSavedAlbumsUseCase)
    factoryOf(::ToggleAlbumSavedUseCase)
    factoryOf(::ToggleListenLaterSavedUseCase)
    factoryOf(::GetLocalAlbumUseCase)
}