package com.m4ykey.core

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.m4ykey.core.ui.AlbumGridCard
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlbumGridCardTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private var receivedId : Int? = null

    private fun setContent(
        id : Int = 1,
        image : String = "",
        albumName : String = "Album",
        artistName : String = "Artist"
    ) {
        composeTestRule.setContent {
            AlbumGridCard(
                id = id,
                albumName = albumName,
                artistName = artistName,
                image = image,
                onAlbumClick = { receivedId = it }
            )
        }
    }

    @Test
    fun clickEmitsAlbumId() {
        setContent()

        assertNull(receivedId)

        composeTestRule
            .onNodeWithText("Album")
            .performClick()

        assertEquals(1, receivedId)
    }

}