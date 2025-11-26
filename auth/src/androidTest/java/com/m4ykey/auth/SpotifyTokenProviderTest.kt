package com.m4ykey.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.m4ykey.auth.model.Auth
import com.m4ykey.auth.service.RemoteAuthService
import com.m4ykey.auth.token.SpotifyTokenProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

private const val TOKEN_LIFESPAN_MS = 3600000L

class SpotifyTokenProviderTest {

    private val mockService = mockk<RemoteAuthService>()
    private val mockDataStore = mockk<DataStore<Preferences>>()

    private val dataStoreFlow = MutableStateFlow(emptyPreferences())

    private lateinit var tokenProvider: SpotifyTokenProvider

    private val accessTokenKey = stringPreferencesKey("access_token")
    private val expireKey = longPreferencesKey("expire_token")

    private val updateDataSlot = slot<suspend (Preferences) -> Preferences>()

    @Before
    fun setup() {
        coEvery { mockDataStore.data } returns dataStoreFlow

        coEvery {
            mockDataStore.updateData(capture(updateDataSlot))
        } coAnswers {
            val currentPrefs = dataStoreFlow.value
            val newPrefs = updateDataSlot.captured.invoke(currentPrefs)
            dataStoreFlow.value = newPrefs
            newPrefs
        }

        tokenProvider = SpotifyTokenProvider(mockService, mockDataStore)
    }

    @Test
    fun getAccessToken_whenExpired_returnsNullAndForcesRefresh() = runTest {
        val expiredTokenTime = System.currentTimeMillis() - 1000
        val expirePrefs = emptyPreferences().toMutablePreferences().apply {
            set(accessTokenKey, "expired_token_123")
            set(expireKey, expiredTokenTime)
        }
        dataStoreFlow.value = expirePrefs

        val result = tokenProvider.getAccessToken()

        assertNull(result)

        coVerify(exactly = 0) { mockService.getAccessToken(any(), any()) }
    }

    @Test
    fun fetchAndSaveNewToken_callsApiAndSavesResult() = runTest {
        val freshToken = "fresh_token"

        coEvery {
            mockService.getAccessToken(any(), any())
        } returns Auth(
            accessToken = freshToken,
            expiresIn = 3600
        )

        val result = tokenProvider.fetchAndSaveNewToken()

        assertEquals(freshToken, result)

        coVerify(exactly = 1) { mockService.getAccessToken(any(), any()) }

        val savedPrefs = dataStoreFlow.first()
        assertEquals(freshToken, savedPrefs[accessTokenKey])

        val savedExpireTime = savedPrefs[expireKey] ?: 0L
        val minExpectedTime = System.currentTimeMillis() + TOKEN_LIFESPAN_MS - 5000
        assert(savedExpireTime > minExpectedTime)
    }

}