package com.m4ykey.albly.album.presentation.new_release

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.albly.album.domain.model.AlbumItem
import com.m4ykey.albly.album.domain.use_case.AlbumUseCase
import kotlinx.coroutines.flow.Flow

class NewReleaseViewModel(
    private val useCase: AlbumUseCase
) : ViewModel() {

    val newRelease : Flow<PagingData<AlbumItem>> = useCase
        .getNewRelease()
        .cachedIn(viewModelScope)


}