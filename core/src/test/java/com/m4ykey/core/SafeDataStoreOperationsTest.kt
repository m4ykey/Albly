package com.m4ykey.core

import com.m4ykey.core.datastore.safeDataStoreOperations
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Test

class SafeDataStoreOperationsTest {

    @Test
    fun `success returns value`() = runTest {
        val result = safeDataStoreOperations { "stored" }
        assertEquals("stored", result)
    }

    @Test
    fun `ioException returns null`() = runTest {
        val result = safeDataStoreOperations<String> { throw IOException("disk full") }
        assertNull(result)
    }

    @Test
    fun `unexpected exception returns null`() = runTest {
        val result = safeDataStoreOperations<String> { throw IllegalStateException("error") }
        assertNull(result)
    }

    @Test
    fun `cancellation is not swallowed`() = runTest {
        try {
            safeDataStoreOperations<Unit> { throw CancellationException("cancelled") }
            fail("CancellationException should be thrown at a higher level")
        } catch (e : CancellationException) {
            assertEquals("cancelled", e.message)
        }
    }

}