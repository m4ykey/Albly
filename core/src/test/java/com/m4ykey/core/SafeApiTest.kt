package com.m4ykey.core

import com.m4ykey.core.network.safeApi
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class SafeApiTest {

    @Test
    fun `returns success value`() = runTest {
        val result = safeApi { "ok" }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `generic exception becomes unexpected error`() = runTest {
        val result = safeApi<String> { throw IllegalStateException("error") }
        assertTrue(result.isFailure)
        assertEquals("Unexpected error: error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `ioException becomes network error`() = runTest {
        val result = safeApi<String> { throw IOException("no wifi") }
        assertTrue(result.isFailure)
        assertEquals("Network error: no wifi", result.exceptionOrNull()?.message)
    }

    @Test
    fun `cancellation is not swallowed`() = runTest {
        try {
            safeApi<Unit> { throw CancellationException("cancelled") }
            fail("CancellationException should be thrown at a higher level")
        } catch (e : CancellationException) {
            assertEquals("cancelled", e.message)
        }
    }

}