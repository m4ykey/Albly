package com.m4ykey.network

import com.m4ykey.lyrics.data.network.service.LyricsService
import com.m4ykey.lyrics.data.network.service.RemoteLyricsService
import com.m4ykey.search.data.network.service.RemoteSearchService
import com.m4ykey.search.data.network.service.SearchService
import io.ktor.client.plugins.defaultRequest
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val LRCLIB = "LrcLibClient"
private val SEARCH_DISCOGS = "SearchDiscogsClient"

val networkModule = module {

    single(named(SEARCH_DISCOGS)) {
        HttpClientFactory.create(enableLogging = true).config {
            defaultRequest {
                url("https://api.discogs.com/database/")
                url.parameters.append("token", BuildConfig.token)
            }
        }
    }

    single(named(LRCLIB)) {
        HttpClientFactory.create(enableLogging = true).config {
            defaultRequest {
                url("https://lrclib.net/api/")
            }
        }
    }

    single<RemoteSearchService> {
        SearchService(httpClient = get(named(SEARCH_DISCOGS)))
    }
    single<RemoteLyricsService> {
        LyricsService(httpClient = get(named(LRCLIB)))
    }
}