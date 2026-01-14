@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package com.m4ykey.track.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.track.data.mapper.toDomain
import com.m4ykey.track.domain.use_case.GetTrackUseCase
import com.m4ykey.track.domain.use_case.TrackUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class TrackViewModel(
    private val useCase : TrackUseCase,
    private val getTrackUseCase: GetTrackUseCase
) : ViewModel() {

    private val _albumId = MutableStateFlow<String?>(null)

    fun setAlbum(id : String?) {
        _albumId.value = id
    }

    val trackResults = _albumId.filterNotNull().flatMapLatest { id ->
        val local = getTrackUseCase(id)
        if (local.isNotEmpty()) {
            flowOf(PagingData.from(local.map { it.toDomain() }))
        } else {
            useCase(id)
        }
    }.cachedIn(viewModelScope)
}