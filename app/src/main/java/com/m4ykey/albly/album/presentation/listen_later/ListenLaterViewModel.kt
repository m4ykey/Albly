package com.m4ykey.albly.album.presentation.listen_later

import androidx.lifecycle.ViewModel
import com.m4ykey.albly.album.domain.use_case.GetListenLaterAlbumsUseCase
import com.m4ykey.albly.album.domain.use_case.GetRandomAlbumUseCase

class ListenLaterViewModel(
    private val getRandomAlbumUseCase: GetRandomAlbumUseCase,
    private val getListenLaterAlbumsUseCase: GetListenLaterAlbumsUseCase
) : ViewModel() {
}