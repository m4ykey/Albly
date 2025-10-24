package com.m4ykey.albly.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _searchType = MutableStateFlow(SearchTypeState())
    val searchType = _searchType.asStateFlow()

    private val _typeUiEvent = MutableSharedFlow<SearchUiEvent>()
    val typeUiEvent = _typeUiEvent.asSharedFlow()

    fun updateType(type : SearchType) {
        _searchType.update { it.copy(type = type) }
    }

    fun onAction(action : SearchTypeAction) {
        viewModelScope.launch {
            val event = when (action) {
                is SearchTypeAction.OnTypeClick -> SearchUiEvent.ChangeType(action.type)
            }
            _typeUiEvent.emit(event)
        }
    }

}