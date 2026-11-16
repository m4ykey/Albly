package com.m4ykey.core

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.m4ykey.core.ui.ActionIconButton
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActionIconButtonTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private var clicks = 0

    private fun setContent(
        textRes : Int = R.string.back,
        iconRes : Int = R.drawable.ic_arrow_left
    ) {
        composeTestRule.setContent {
            ActionIconButton(
                onClick = { clicks++ },
                iconRes = iconRes,
                textRes = textRes
            )
        }
    }

    @Test
    fun contentDescriptionComesFromTextRes() {
        val label = composeTestRule.activity.getString(R.string.back)

        setContent()

        composeTestRule
            .onNodeWithContentDescription(label)
            .assertIsDisplayed()
    }

    @Test
    fun clickCallsOnClick() {
        val label = composeTestRule.activity.getString(R.string.back)

        setContent()

        composeTestRule
            .onNodeWithContentDescription(label)
            .performClick()

        assertEquals(1, clicks)
    }

}