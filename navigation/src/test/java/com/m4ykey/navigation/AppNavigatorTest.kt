package com.m4ykey.navigation

import androidx.navigation3.runtime.NavKey

class AppNavigatorTest {

    private val backStack = mutableListOf<NavKey>(Route.Collection)
    private val navigator = AppNavigator(backStack)

}