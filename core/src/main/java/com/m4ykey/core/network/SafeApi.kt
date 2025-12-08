package com.m4ykey.core.network

import java.io.IOException

suspend fun <T> safeApi(api : suspend () -> T) : Result<T> {
    return try {
        Result.success(api())
    } catch (e: Exception) {
        Result.failure(Exception("Unexpected error: ${e.message}"))
    } catch (e : IOException) {
        Result.failure(Exception("Network error: ${e.message}"))
    }
}