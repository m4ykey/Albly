package com.m4ykey.navigation

import androidx.navigation3.runtime.NavKey

class AppNavigator(private val backStack : MutableList<NavKey>) {

    fun navigateTo(route: Route) {
        if (backStack.lastOrNull() == route) return
        backStack.add(route)
    }

    fun navigateBack() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    }

}