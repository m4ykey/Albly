package com.m4ykey.settings.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.m4ykey.settings.R

enum class ThemeType(val index : Int) {
    DEFAULT(0),
    LIGHT(1),
    DARK(2);

    companion object {
        fun fromIndex(index : Int) : ThemeType {
            return entries.find { it.index == index } ?: DEFAULT
        }
    }
}

@Composable
fun ThemeType.label() : String = when (this) {
    ThemeType.DEFAULT -> stringResource(R.string.default_mode)
    ThemeType.DARK -> stringResource(R.string.dark_mode)
    ThemeType.LIGHT -> stringResource(R.string.light_mode)
}