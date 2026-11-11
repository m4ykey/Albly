package com.m4ykey.album.presentation.video

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AlbumVideoViewModel : ViewModel() {

    private val _videos = MutableStateFlow<List<String>>(emptyList())
    val videos = _videos.asStateFlow()

    fun setVideos(videoList : List<String>) {
        _videos.value = videoList
    }

}