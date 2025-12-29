package com.m4ykey.albly.di

import com.m4ykey.albly.album.domain.use_case.AlbumUseCase
import com.m4ykey.albly.album.domain.use_case.GetAlbumStateUseCase
import com.m4ykey.albly.album.domain.use_case.GetListenLaterAlbumsUseCase
import com.m4ykey.albly.album.domain.use_case.GetLocalAlbumUseCase
import com.m4ykey.albly.album.domain.use_case.GetRandomAlbumUseCase
import com.m4ykey.albly.album.domain.use_case.GetSavedAlbumsUseCase
import com.m4ykey.albly.album.domain.use_case.NewReleaseUseCase
import com.m4ykey.albly.album.domain.use_case.ToggleAlbumSavedUseCase
import com.m4ykey.albly.album.domain.use_case.ToggleListenLaterSavedUseCase
import com.m4ykey.albly.search.domain.use_case.SearchUseCase
import com.m4ykey.albly.track.domain.use_case.DeleteTracksUseCase
import com.m4ykey.albly.track.domain.use_case.GetTrackUseCase
import com.m4ykey.albly.track.domain.use_case.SaveTracksUseCase
import com.m4ykey.albly.track.domain.use_case.TrackUseCase
import org.koin.dsl.module

val searchUseCaseModule = module {
    single { SearchUseCase(get()) }
}

val albumUseCaseModule = module {
    single { AlbumUseCase(get()) }
    single { NewReleaseUseCase(get()) }
    single { GetAlbumStateUseCase(get()) }
    single { GetListenLaterAlbumsUseCase(get()) }
    single { GetRandomAlbumUseCase(get()) }
    single { GetSavedAlbumsUseCase(get()) }
    single { ToggleAlbumSavedUseCase(
        repository = get(),
        saveTracksUseCase = get(),
        deleteTracksUseCase = get()
    ) }
    single { ToggleListenLaterSavedUseCase(
        repository = get(),
        saveTracksUseCase = get(),
        deleteTracksUseCase = get()
    ) }
    single { GetLocalAlbumUseCase(get()) }
}

val trackUseCaseModule = module {
    single { TrackUseCase(get()) }
    single { SaveTracksUseCase(get()) }
    single { GetTrackUseCase(get()) }
    single { DeleteTracksUseCase(get()) }
}