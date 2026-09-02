package com.m4ykey.core.datastore

import kotlinx.io.IOException

suspend fun <T> safeDataStoreOperations(operation : suspend () -> T) : T? {
    return runCatching {
        operation()
    }.onFailure { exception ->
        if (exception is IOException) {
            exception.printStackTrace()
        }
    }.getOrNull()
}