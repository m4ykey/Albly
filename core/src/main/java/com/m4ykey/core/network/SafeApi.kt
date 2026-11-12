package com.m4ykey.core.network

import io.ktor.utils.io.CancellationException
import kotlinx.io.IOException

suspend fun <T> safeApi(api : suspend () -> T) : Result<T> {
    return try {
        Result.success(api())
    } catch (e : CancellationException) {
        throw e
    } catch (e : IOException) {
        Result.failure(Exception("Network error: ${e.message}", e))
    } catch (e : Exception) {
        Result.failure(Exception("Unexpected error: ${e.message}", e))
    }
}