package com.m4ykey.core.network

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

fun openLink(link : String, context : Context) {
    val normalized = if ("://" in link) link else "https://$link"
    val uri = normalized.toUri()

    val scheme = uri.scheme?.lowercase()
    if (scheme != "http" && scheme != "https") return

    try {
        CustomTabsIntent.Builder().build().apply {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            launchUrl(context, uri)
        }
    } catch (_: ActivityNotFoundException) {

    }
}