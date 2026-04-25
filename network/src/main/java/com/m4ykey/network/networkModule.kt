package com.m4ykey.network

import com.m4ykey.album.data.network.service.AlbumService
import com.m4ykey.album.data.network.service.NewReleaseAlbumService
import com.m4ykey.album.data.network.service.RemoteAlbumService
import com.m4ykey.album.data.network.service.RemoteNewReleaseAlbumService
import com.m4ykey.lyrics.data.network.service.LyricsService
import com.m4ykey.lyrics.data.network.service.RemoteLyricsService
import com.m4ykey.search.data.network.service.RemoteSearchService
import com.m4ykey.search.data.network.service.SearchService
import io.ktor.client.plugins.defaultRequest
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val LRCLIB = "LrcLibClient"
private const val SEARCH_DISCOGS = "SearchDiscogsClient"
private const val ALBUM_DISCOGS = "AlbumDiscogsClient"
private const val NEW_RELEASE = "NewReleaseDiscogsClient"

val networkModule = module {

    single(named(ALBUM_DISCOGS)) {
        HttpClientFactory.create(enableLogging = true).config {
            defaultRequest {
                url("https://api.discogs.com/")
                url.parameters.append("token", BuildConfig.token)
            }
        }
    }

    single(named(SEARCH_DISCOGS)) {
        HttpClientFactory.create(enableLogging = true).config {
            defaultRequest {
                url("https://api.discogs.com/database/")
                url.parameters.append("token", BuildConfig.token)
            }
        }
    }

    single(named(NEW_RELEASE)) {
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

    single<RemoteNewReleaseAlbumService> {
        NewReleaseAlbumService(httpClient = get(named(NEW_RELEASE)))
    }

    single<RemoteAlbumService> {
        AlbumService(httpClient = get(named(ALBUM_DISCOGS)))
    }

    single<RemoteSearchService> {
        SearchService(httpClient = get(named(SEARCH_DISCOGS)))
    }
    single<RemoteLyricsService> {
        LyricsService(httpClient = get(named(LRCLIB)))
    }
}