package com.m4ykey.albly.util.network

import okio.IOException
import retrofit2.HttpException

suspend fun <T> safeApi(api : suspend () -> T) : Result<T> {
    return try {
        Result.success(api())
    } catch (e: Exception) {
        Result.failure(Exception("Unexpected error: ${e.message}"))
    } catch (e : HttpException) {
        Result.failure(Exception("Http error: ${e.code()} - ${e.message}"))
    } catch (e : IOException) {
        Result.failure(Exception("Network error: ${e.message}"))
    }
}