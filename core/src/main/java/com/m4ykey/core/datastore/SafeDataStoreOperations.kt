package com.m4ykey.core.datastore

import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException

suspend fun <T> safeDataStoreOperations(operation : suspend () -> T) : T? {
    return try {
        operation()
    } catch (e : CancellationException) {
        throw e
    } catch (e : IOException) {
        e.printStackTrace()
        null
    } catch (e : Exception) {
        null
    }
}