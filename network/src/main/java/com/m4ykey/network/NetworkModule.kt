package com.m4ykey.network

import com.m4ykey.album.data.network.service.AlbumService
import com.m4ykey.album.data.network.service.NewReleaseAlbumService
import com.m4ykey.album.data.network.service.RemoteAlbumService
import com.m4ykey.album.data.network.service.RemoteNewReleaseAlbumService
import com.m4ykey.lyrics.data.service.LyricsService
import com.m4ykey.lyrics.data.service.RemoteLyricsService
import com.m4ykey.network.BuildConfig.genius_token
import com.m4ykey.network.BuildConfig.token
import com.m4ykey.search.data.network.service.RemoteSearchLyricsService
import com.m4ykey.search.data.network.service.RemoteSearchService
import com.m4ykey.search.data.network.service.SearchLyricsService
import com.m4ykey.search.data.network.service.SearchService
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val LRCLIB = "LrcLibClient"
private const val SEARCH_DISCOGS = "SearchDiscogsClient"
private const val ALBUM_DISCOGS = "AlbumDiscogsClient"
private const val NEW_RELEASE = "NewReleaseDiscogsClient"
private const val GENIUS_SEARCH = "GeniusSearchClient"

private const val GENIUS_DEFAULT_URL = "https://api.genius.com/"
private const val DISCOGS_DEFAULT_URL = "https://api.discogs.com/"
private const val DISCOGS_DATABASE_URL = "https://api.discogs.com/database/"
private const val LRCLIB_URL = "https://lrclib.net/api/"

val networkModule = module {

    single(named(GENIUS_SEARCH)) {
        HttpClientFactory.create(baseUrl = GENIUS_DEFAULT_URL, token = genius_token)
    }

    single(named(ALBUM_DISCOGS)) {
        HttpClientFactory.create(baseUrl = DISCOGS_DEFAULT_URL, token = token, isTokenInUrl = true)
    }

    single(named(SEARCH_DISCOGS)) {
        HttpClientFactory.create(baseUrl = DISCOGS_DATABASE_URL, token = token, isTokenInUrl = true)
    }

    single(named(NEW_RELEASE)) {
        HttpClientFactory.create(baseUrl = DISCOGS_DATABASE_URL, token = token, isTokenInUrl = true)
    }

    single(named(LRCLIB)) {
        HttpClientFactory.create(baseUrl = LRCLIB_URL)
    }

    single<RemoteSearchLyricsService> {
        SearchLyricsService(httpClient = get(named(GENIUS_SEARCH)))
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