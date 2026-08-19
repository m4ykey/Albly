package com.m4ykey.settings

import com.m4ykey.settings.theme.ThemeType
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class ThemeTypeTest {

    @Test
    fun `fromIndex should return correct theme`() = runTest {
        assertEquals(
            ThemeType.DEFAULT,
            ThemeType.fromIndex(0)
        )

        assertEquals(
            ThemeType.LIGHT,
            ThemeType.fromIndex(1)
        )

        assertEquals(
            ThemeType.DARK,
            ThemeType.fromIndex(2)
        )
    }

    @Test
    fun `fromIndex should return default for unknown index`() = runTest {
        assertEquals(
            ThemeType.DEFAULT,
            ThemeType.fromIndex(-1)
        )

        assertEquals(
            ThemeType.DEFAULT,
            ThemeType.fromIndex(123)
        )
    }

}