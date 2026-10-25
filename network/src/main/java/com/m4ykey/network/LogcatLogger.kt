package com.m4ykey.network

import android.util.Log
import io.ktor.client.plugins.logging.Logger

object LogcatLogger : Logger {
    override fun log(message: String) {
        message.chunked(3000).forEach { chunk ->
            Log.d("Ktor", chunk)
        }
    }
}