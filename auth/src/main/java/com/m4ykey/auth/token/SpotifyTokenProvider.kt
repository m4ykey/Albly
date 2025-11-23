package com.m4ykey.auth.token

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.m4ykey.auth.BuildConfig
import com.m4ykey.auth.service.RemoteAuthService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import kotlin.time.ExperimentalTime

class SpotifyTokenProvider(
    private val service : RemoteAuthService,
    private val dataStore : DataStore<Preferences>
) : TokenProvider {

    private val accessTokenKey = stringPreferencesKey("access_token")
    private val expireKey = longPreferencesKey("expire_token")

    override suspend fun getAccessToken(): String? {
        return try {
            val preferences = dataStore.data.firstOrNull() ?: return null
            val cachedToken = preferences[accessTokenKey]
            val expireTime = preferences[expireKey] ?: 0L

            if (!cachedToken.isNullOrBlank() && System.currentTimeMillis() < expireTime) {
                cachedToken
            } else {
                null
            }
        } catch (e : Exception) {
            println("Error reading access token from DataStore: ${e.message}")
            null
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun fetchAndSaveNewToken() : String {
        return try {
            val newAccessToken = fetchAccessToken(
                service = service,
                clientId = BuildConfig.SPOTIFY_CLIENT_ID,
                clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET
            )

            val expiresInHours = 1

            val expirationTimeInstant = kotlin.time.Clock.System.now().plus(expiresInHours,
                DateTimeUnit.HOUR)

            val newExpireTime = expirationTimeInstant.toEpochMilliseconds()

            saveAccessToken(newAccessToken, newExpireTime)

            newAccessToken
        } catch (e : Exception) {
            throw RuntimeException("Failed to fetch and save new access token", e)
        }
    }

    private suspend fun saveAccessToken(token : String?, expireTime : Long) {
        if (token != null) {
            dataStore.edit { preferences ->
                preferences[accessTokenKey] = token
                preferences[expireKey] = expireTime
            }
        }
    }
}