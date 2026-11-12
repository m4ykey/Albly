package com.m4ykey.navigation

import androidx.navigation3.runtime.NavKey
import org.junit.Test
import org.junit.Assert.assertEquals

class AppNavigatorTest {

    @Test
    fun navigateToAddingRoute() {
        val stack = mutableListOf<NavKey>(Route.Collection)
        val navigator = AppNavigator(stack)

        navigator.navigateTo(Route.AlbumDetail(1))

        assertEquals(
            listOf(Route.Collection, Route.AlbumDetail(1)),
            stack.toList()
        )
    }

    @Test
    fun navigateToIgnoringDuplicateOnTop() {
        val stack = mutableListOf<NavKey>(Route.Collection)
        val navigator = AppNavigator(stack)

        navigator.navigateTo(Route.AlbumDetail(1))
        navigator.navigateTo(Route.AlbumDetail(1))

        assertEquals(
            listOf(Route.Collection, Route.AlbumDetail(1)),
            stack.toList()
        )
    }

    @Test
    fun navigateToAllowingDuplicateNotOnTop() {
        val stack = mutableListOf<NavKey>(Route.Collection)
        val navigator = AppNavigator(stack)

        navigator.navigateTo(Route.AlbumDetail(1))
        navigator.navigateTo(Route.Search)
        navigator.navigateTo(Route.AlbumDetail(1))

        assertEquals(
            listOf(
                Route.Collection,
                Route.AlbumDetail(1),
                Route.Search,
                Route.AlbumDetail(1)
            ),
            stack.toList()
        )
    }

    @Test
    fun navigateBackPoppingTop() {
        val stack = mutableListOf<NavKey>(Route.Collection)
        val navigator = AppNavigator(stack)

        navigator.navigateTo(Route.AlbumDetail(1))
        navigator.navigateBack()

        assertEquals(
            listOf(Route.Collection),
            stack.toList()
        )
    }

    @Test
    fun navigateBackOnRootDoingNothing() {
        val stack = mutableListOf<NavKey>(Route.Collection)
        val navigator = AppNavigator(stack)

        navigator.navigateBack()

        assertEquals(
            listOf(Route.Collection),
            stack.toList()
        )
    }
}