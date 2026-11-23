package com.m4ykey.settings

import android.content.Context
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.util.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class LibrariesProvider(private val context : Context) {

    private val mutex = Mutex()

    @Volatile
    var cached : Libs? = null
        private set

    suspend fun get() : Libs {
        cached?.let { return it }

        return mutex.withLock {
            cached ?: withContext(Dispatchers.IO) {
                Libs.Builder()
                    .withContext(context)
                    .build()
                    .also { cached = it }
            }
        }
    }
}