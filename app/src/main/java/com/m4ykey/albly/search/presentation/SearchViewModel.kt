package com.m4ykey.albly.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.m4ykey.albly.search.domain.use_case.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchViewModel @Inject constructor(
    private val useCase: SearchUseCase
): ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchType = MutableStateFlow(SearchTypeState())
    val searchType = _searchType.asStateFlow()

    private val _typeUiEvent = MutableSharedFlow<SearchUiEvent>()
    val typeUiEvent = _typeUiEvent.asSharedFlow()

    fun updateType(type : SearchType) {
        _searchType.update { it.copy(type = type) }
    }

    val searchResults = combine(
        _searchQuery.debounce(500),
        _searchType
    ) { query, typeState ->
        query to typeState.type
    }.flatMapLatest { (query, type) ->
        if (query.isBlank()) emptyFlow()
        else useCase(q = query, type = type.name.lowercase()).cachedIn(viewModelScope)
    }

    fun onAction(action : SearchTypeAction) {
        viewModelScope.launch {
            when (action) {
                is SearchTypeAction.OnTypeClick -> {
                    updateType(action.type)
                    _typeUiEvent.emit(SearchUiEvent.ChangeType(action.type))
                }
                is SearchTypeAction.OnQueryChange -> {
                    _searchQuery.value = action.query
                }
            }
        }
    }
}