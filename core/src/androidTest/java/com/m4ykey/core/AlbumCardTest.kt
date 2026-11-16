package com.m4ykey.core

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.m4ykey.core.ui.AlbumCard
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class AlbumCardTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private var clicks = 0
    private var longClicks = 0

    private fun setContent(
        albumName : String = "Album1",
        artistName : String = "Artist1",
        image : String = ""
    ) {
        composeTestRule.setContent {
            AlbumCard(
                albumName = albumName,
                artistName = artistName,
                image = image,
                onAlbumClick = { clicks++ },
                onLongAlbumClick = { longClicks++ }
            )
        }
    }

    @Test
    fun clickCallsOnlyOnAlbumClick() {
        setContent()

        composeTestRule
            .onNodeWithText("Album1")
            .performClick()

        assertEquals(1, clicks)
        assertEquals(0, longClicks)
    }

    @Test
    fun longClickCallsOnlyOnLongAlbumClicks() {
        setContent()

        composeTestRule
            .onNodeWithText("Album1")
            .performTouchInput { longClick() }

        assertEquals(0, clicks)
        assertEquals(1, longClicks)
    }

}