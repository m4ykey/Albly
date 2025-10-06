package com.m4ykey.albly.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey

class TopLevelBackStack<T : NavKey>(private val startKey : T) {

    private var topLevelBackStack: HashMap<T, SnapshotStateList<T>> = hashMapOf(
        startKey to mutableStateListOf(startKey)
    )

    var topLevelKey by mutableStateOf(startKey)
        private set

    val backStack = mutableStateListOf(startKey)

    private fun updateBackStack() {
        backStack.clear()
        val currentBackStack = topLevelBackStack[topLevelKey] ?: emptyList()

        if (topLevelKey == startKey) {
            backStack.addAll(currentBackStack)
        } else {
            val startStack = topLevelBackStack[startKey] ?: emptyList()
            backStack.addAll(startStack + currentBackStack)
        }
    }

    fun switchTopLevel(key : T) {
        if (topLevelBackStack[key] == null) {
            topLevelBackStack[key] = mutableStateListOf(key)
        }
        topLevelKey = key
        updateBackStack()
    }

    fun add(key : T) {
        topLevelBackStack[topLevelKey]?.add(key)
        updateBackStack()
    }

    fun removeLast() {
        val currentBackStack = topLevelBackStack[topLevelKey] ?: return

        if (currentBackStack.size > 1) {
            currentBackStack.removeLastOrNull()
        } else if (topLevelKey != startKey) {
            topLevelKey = startKey
        }
        updateBackStack()
    }

    fun replaceStack(vararg key: T) {
        topLevelBackStack[topLevelKey] = mutableStateListOf(*key)
        updateBackStack()
    }
}