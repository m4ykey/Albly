package com.m4ykey.albly.ui.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel : ViewModel() {

    private val _navStack = MutableStateFlow(TopLevelBackStack<Screen>(CollectionScreen))
    val navStack = _navStack.asStateFlow()

    val backStack : SnapshotStateList<Screen>
        get() = _navStack.value.backStack

    fun navigateTo(screen: Screen) {
        _navStack.value.add(screen)
    }

    fun switchTopLevel(screen: Screen) {
        _navStack.value.switchTopLevel(screen)
    }

    fun goBack() {
        _navStack.value.removeLast()
    }

}