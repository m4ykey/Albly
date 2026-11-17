package com.m4ykey.albly.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun startupProfile() = rule.collect(
        packageName = PACKAGE_NAME,
        includeInStartupProfile = true
    ) {
        pressHome()
        startActivityAndWait()

        device.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), 5_000)
        device.waitForIdle()
    }

    @Test
    fun journeyProfile() = rule.collect(packageName = PACKAGE_NAME) {
        pressHome()
        startActivityAndWait()

        device.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), 5_000)
        device.waitForIdle()

        repeat(2) { device.flingFirstScrollable(Direction.DOWN) }

        device.clickAny("Menu")
        device.clickAny("New release", "Nowe wydania")
        device.waitForIdle()

        device.pressBack()
        device.waitForIdle()

        device.clickAny("Search", "Szukaj")
        device.waitForIdle()

        device.pressBack()
        device.waitForIdle()
    }

    private fun UiDevice.flingFirstScrollable(direction: Direction) {
        findObject(By.scrollable(true))?.fling(direction)
        waitForIdle()
    }

    private fun UiDevice.clickAny(vararg labels : String) : Boolean {
        val target = labels.firstNotNullOfOrNull { label ->
            findObject(By.text(label)) ?: findObject(By.desc(label))
        }

        return target?.click() != null
    }

    companion object {
        private const val PACKAGE_NAME = "com.m4ykey.albly"
    }

}