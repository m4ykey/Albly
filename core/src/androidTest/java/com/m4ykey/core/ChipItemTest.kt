package com.m4ykey.core

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.m4ykey.core.ui.ChipItem
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChipItemTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val selections = mutableListOf<Boolean>()

    private fun setContent(
        selected : Boolean = false,
        label : String = Label.ITEM,
        isLeadingIcon : Boolean = false,
        isTrailingIcon : Boolean = false
    ) {
        composeTestRule.setContent {
            ChipItem(
                label = label,
                selected = selected,
                isLeadingIcon = isLeadingIcon,
                isTrailingIcon = isTrailingIcon,
                onSelect = { selections += it }
            )
        }
    }

    @Test
    fun clickingUnselectedChipEmitsTrue() {
        setContent(selected = false)

        composeTestRule
            .onNodeWithText(Label.ITEM)
            .assertIsNotSelected()

        composeTestRule
            .onNodeWithText(Label.ITEM)
            .performClick()

        assertEquals(listOf(true), selections)
    }

    @Test
    fun clickingSelectedChipEmitsFalse() {
        setContent(selected = true)

        composeTestRule
            .onNodeWithText(Label.ITEM)
            .assertIsSelected()

        composeTestRule
            .onNodeWithText(Label.ITEM)
            .performClick()

        assertEquals(listOf(false), selections)
    }

    @Test
    fun iconIsVisibleWhenSelected() {
        var selected by mutableStateOf(false)

        composeTestRule.setContent {
            ChipItem(
                label = Label.ITEM,
                selected = selected,
                onSelect = { selected = it },
                isLeadingIcon = true
            )
        }

        composeTestRule
            .onNodeWithContentDescription(Label.ITEM, useUnmergedTree = true)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(Label.ITEM)
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(Label.ITEM, useUnmergedTree = true)
            .assertExists()
    }

}

private object Label {
    const val ITEM = "Item"
}