@file:OptIn(ExperimentalTestApi::class)

package com.m4ykey.navigation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationRootTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val backStack = NavBackStack<NavKey>(Route.Collection)

    private fun setContent() {
        composeTestRule.setContent {
            NavigationRoot(
                backStack = backStack,
                entryProviderFactory = { navigator -> fakeEntries(navigator) }
            )
        }
    }

    private fun assertStack(vararg excepted : Route) {
        composeTestRule.runOnIdle {
            assertEquals(excepted.toList(), backStack.toList())
        }
    }

    @Test
    fun startsOnCollection() {
        setContent()

        composeTestRule.onNodeWithTag("screen_collection").assertIsDisplayed()
        assertStack(Route.Collection)
    }

    @Test
    fun openAlbumPushesDetailWithArgument() {
        setContent()

        composeTestRule.onNodeWithTag(Tags.OPEN_ALBUM).performClick()

        composeTestRule.waitUntilExactlyOneExists(hasTestTag(Tags.ALBUM))

        composeTestRule.onNodeWithText("albumId=1").assertIsDisplayed()

        assertStack(Route.Collection, Route.AlbumDetail(1))
    }

    @Test
    fun backFromAlbumReturnsToCollection() {
        setContent()

        composeTestRule.onNodeWithTag(Tags.OPEN_ALBUM).performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(Tags.ALBUM))

        composeTestRule.onNodeWithTag(Tags.BACK).performClick()

        composeTestRule.waitUntilDoesNotExist(hasTestTag(Tags.ALBUM))
        composeTestRule.onNodeWithTag(Tags.COLLECTION).assertIsDisplayed()

        assertStack(Route.Collection)
    }

    @Test
    fun openLyricsPushesLyricsWithArgument() {
        setContent()

        composeTestRule.onNodeWithTag(Tags.OPEN_LYRICS).performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(Tags.LYRICS))

        composeTestRule.onNodeWithText("track=Track").assertIsDisplayed()
        composeTestRule.onNodeWithText("img=null").assertIsDisplayed()
        composeTestRule.onNodeWithText("album=null").assertIsDisplayed()

        assertStack(
            Route.Collection,
            Route.Lyrics(
                track = "Track",
                artist = "Artist",
                img = null,
                album = null
            )
        )
    }

    @Test
    fun backFromAlbumReturnsToCollectionWithEspresso() {
        setContent()

        composeTestRule.onNodeWithTag(Tags.OPEN_ALBUM).performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(Tags.ALBUM))

        Espresso.pressBack()

        composeTestRule.waitUntilDoesNotExist(hasTestTag(Tags.ALBUM))
        composeTestRule.onNodeWithTag(Tags.COLLECTION).assertIsDisplayed()

        assertStack(Route.Collection)
    }

    @Test
    fun restoresAlbumDetailAfterStateRestore() {
        val tester = StateRestorationTester(composeTestRule)

        tester.setContent {
            NavigationRoot(
                entryProviderFactory = { navigator ->
                    fakeEntries(navigator)
                }
            )
        }

        composeTestRule.onNodeWithTag(Tags.OPEN_ALBUM).performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(Tags.ALBUM))

        tester.emulateSavedInstanceStateRestore()

        composeTestRule.waitUntilExactlyOneExists(hasTestTag(Tags.ALBUM))
        composeTestRule.onNodeWithText("albumId=1").assertIsDisplayed()

        Espresso.pressBack()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(Tags.ALBUM))
        composeTestRule.onNodeWithTag(Tags.COLLECTION).assertIsDisplayed()
    }

    @Test
    fun restoreLyricsWithNullableArgumentsAfterStateRestore() {
        val tester = StateRestorationTester(composeTestRule)

        tester.setContent {
            NavigationRoot(
                entryProviderFactory = { navigator ->
                    fakeEntries(navigator)
                }
            )
        }

        composeTestRule.onNodeWithTag(Tags.OPEN_LYRICS).performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(Tags.LYRICS))

        tester.emulateSavedInstanceStateRestore()

        composeTestRule.waitUntilExactlyOneExists(hasTestTag(Tags.LYRICS))
        composeTestRule.onNodeWithText("track=Track").assertIsDisplayed()
        composeTestRule.onNodeWithText("img=null").assertIsDisplayed()
        composeTestRule.onNodeWithText("album=null").assertIsDisplayed()

        Espresso.pressBack()

        composeTestRule.waitUntilDoesNotExist(hasTestTag(Tags.LYRICS))
        composeTestRule.onNodeWithTag(Tags.COLLECTION).assertIsDisplayed()
    }

}

private fun fakeEntries(navigator: AppNavigator) : (NavKey) -> NavEntry<NavKey> =
    entryProvider {
        entry<Route.Collection> {
            Column(Modifier.testTag(Tags.COLLECTION)) {
                Button(
                    modifier = Modifier.testTag(Tags.OPEN_ALBUM),
                    onClick = { navigator.navigateTo(Route.AlbumDetail(1)) }
                ) {
                    Text("open album")
                }

                Button(
                    onClick = {
                        navigator.navigateTo(Route.Lyrics(track = "Track", artist = "Artist", img = null, album = null))
                    },
                    modifier = Modifier.testTag(Tags.OPEN_LYRICS)
                ) {
                    Text("open lyrics")
                }
            }
        }

        entry<Route.AlbumDetail> { key ->
            Column(modifier = Modifier.testTag(Tags.ALBUM)) {
                Text("albumId=${key.albumId}")
                Button(
                    modifier = Modifier.testTag(Tags.BACK),
                    onClick = { navigator.navigateBack() }
                ) {
                    Text("back")
                }
            }
        }

        entry<Route.Lyrics> { key ->
            Column(modifier = Modifier.testTag(Tags.LYRICS)) {
                Text("track=${key.track}")
                Text("img=${key.img}")
                Text("album=${key.album}")
            }
        }
    }

private object Tags {
    const val COLLECTION = "screen_collection"
    const val LYRICS = "screen_lyrics"
    const val ALBUM = "screen_album"
    const val BACK = "back"
    const val OPEN_LYRICS = "open_lyrics"
    const val OPEN_ALBUM = "open_album"
}