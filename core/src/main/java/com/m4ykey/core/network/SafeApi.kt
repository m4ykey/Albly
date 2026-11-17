package com.m4ykey.core.network

import io.ktor.utils.io.CancellationException
import kotlinx.io.IOException
import java.nio.channels.UnresolvedAddressException

suspend fun <T> safeApi(api : suspend () -> T) : Result<T> {
    return try {
        Result.success(api())
    } catch (e : CancellationException) {
        throw e
    } catch (e : UnresolvedAddressException) {
        Result.failure(IOException("Network error: no internet connection", e))
    } catch (e : IOException) {
        Result.failure(Exception("Network error: ${e.message}", e))
    } catch (e : Exception) {
        Result.failure(Exception("Unexpected error: ${e.message ?: e::class.simpleName}", e))
    }
}