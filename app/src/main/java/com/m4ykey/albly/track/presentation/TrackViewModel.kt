package com.m4ykey.albly.track.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.m4ykey.albly.track.domain.use_case.TrackUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class TrackViewModel(
    private val useCase : TrackUseCase
) : ViewModel() {

    private val _albumId = MutableStateFlow<String?>(null)

    fun setAlbum(id : String?) {
        _albumId.value = id
    }

    val trackResults = _albumId
        .filterNotNull()
        .flatMapLatest { id -> useCase.invoke(id = id).cachedIn(viewModelScope) }

}