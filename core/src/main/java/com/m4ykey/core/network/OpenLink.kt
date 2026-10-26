package com.m4ykey.core.network

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

fun openLink(link : String, context : Context) {
    val intent = CustomTabsIntent.Builder().build().intent.apply {
        data = link.toUri()
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}