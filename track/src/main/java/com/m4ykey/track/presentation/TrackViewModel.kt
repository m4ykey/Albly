@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package com.m4ykey.track.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.m4ykey.track.data.mapper.toDomain
import com.m4ykey.track.domain.use_case.GetTrackUseCase
import com.m4ykey.track.domain.use_case.TrackUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
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
            val localPaging = getTrackUseCase(id).map { pagingData ->
                pagingData.map { it.toDomain() }
            }
            val remotePaging = useCase(id).timeout(3.seconds).catch {
                emit(PagingData.empty())
            }

            flowOf(localPaging, remotePaging).flattenConcat()
        }
        .cachedIn(viewModelScope)
}