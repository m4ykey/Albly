package com.m4ykey.core.ext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.m4ykey.core.R

fun copyText(text : String, context : Context) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("copied label", text)
    clipboard.setPrimaryClip(clip)
}