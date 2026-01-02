package com.m4ykey.core.ext

import java.util.Locale

fun formatTrackDuration(seconds : Int) : String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format(Locale.getDefault(), "%d:%02d", minutes, remainingSeconds)
}