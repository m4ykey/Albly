package com.m4ykey.core

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.m4ykey.core.ui.ArtistCard
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArtistCardTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private var receivedId : Int? = null

    private fun setContent(
        title : String = "Title",
        id : Int = 1,
        cover : String = ""
    ) {
        composeTestRule.setContent {
            ArtistCard(
                title = title,
                onArtistClick = { receivedId = it },
                id = id,
                cover = cover
            )
        }
    }

    @Test
    fun clickEmitsArtistId() {
        setContent()

        assertNull(receivedId)

        composeTestRule
            .onNodeWithText("Title")
            .performClick()

        assertEquals(1, receivedId)
    }

}