@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package com.m4ykey.albly.track.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.albly.core.mapper.toDomain
import com.m4ykey.albly.track.domain.use_case.GetTrackUseCase
import com.m4ykey.albly.track.domain.use_case.TrackUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.timeout
import kotlin.time.Duration.Companion.seconds

class TrackViewModel(
    private val useCase : TrackUseCase,
    private val getTrackUseCase: GetTrackUseCase
) : ViewModel() {

    private val _albumId = MutableStateFlow<String?>(null)

    fun setAlbum(id : String?) {
        _albumId.value = id
    }

    val trackResults = _albumId
        .filterNotNull()
        .flatMapLatest { id ->
            useCase.invoke(id = id)
                .timeout(0.5.seconds)
                .catch {
                    val localTracks = getTrackUseCase.invoke(id = id)
                    emit(PagingData.from(localTracks.map { it.toDomain() }))
                }
        }
        .cachedIn(viewModelScope)

}