package com.m4ykey.lyrics.domain.usecase

import com.m4ykey.lyrics.domain.model.LyricsItem
import com.m4ykey.lyrics.domain.model.LyricsRedItem
import com.m4ykey.lyrics.domain.repository.LyricsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.cancellation.CancellationException

class GetLyricsUseCase(
    private val repository: LyricsRepository
) {
    operator fun invoke(
        artist: String,
        track: String,
        album: String?
    ): Flow<LyricsItem> = flow {
        fetchFromLrclib(artist, track, album)?.let {
            emit(it)
            return@flow
        }
        emit(fetchFromRedLyrics(artist, track))
    }

    private suspend fun fetchFromLrclib(
        artist: String,
        track: String,
        album: String?
    ): LyricsItem? = try {
        val results = repository.searchLyrics(
            trackName = track,
            artistName = artist,
            albumName = album
        ).first()

        val match = results.firstOrNull { item ->
            item.trackName.equals(track, ignoreCase = true) &&
                    item.artistName.equals(artist, ignoreCase = true) &&
                    (album == null || item.albumName.equals(album, ignoreCase = true))
        }

        match?.let { repository.getLyrics(it.id).first() }
            ?.takeIf { it.plainLyrics.isNotBlank() || it.syncedLyrics.isNotBlank() }
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        null
    }

    private suspend fun fetchFromRedLyrics(
        artist: String,
        track: String
    ): LyricsItem {
        val queries = listOf("$artist $track", track).distinct()

        val candidates = LinkedHashMap<String, LyricsRedItem>()
        var searchError: Exception? = null

        for (query in queries) {
            try {
                repository.searchRedLyrics(query).first().forEach { item ->
                    if (item.isrc.isNotBlank()) candidates.putIfAbsent(item.isrc, item)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                searchError = e
            }
        }

        if (candidates.isEmpty()) {
            throw Exception(
                if (searchError != null)
                    "Red Lyrics search failed: ${searchError.message}"
                else
                    "Red Lyrics returned no results for \"$artist - $track\"",
                searchError
            )
        }

        val ranked = candidates.values
            .map { it to score(it, artist, track) }
            .sortedByDescending { it.second }
            .map { it.first }
            .take(5)

        var lastError: Exception? = null
        for (item in ranked) {
            try {
                val text = repository.getRedLyrics(item.isrc).first()
                if (text.isNotBlank()) {
                    return LyricsItem(
                        albumName = item.album,
                        artistName = item.artist,
                        id = 0,
                        name = item.title,
                        plainLyrics = text,
                        syncedLyrics = "",
                        trackName = item.title
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                lastError = e
            }
        }

        throw Exception(
            "Red Lyrics found ${ranked.size} result(s) but none had lyrics" +
                    (lastError?.let { ": ${it.message}" } ?: ""),
            lastError
        )
    }

    private fun score(item: LyricsRedItem, artist: String, track: String): Int {
        val title = normalize(item.title)
        val itemArtist = normalize(item.artist)
        val wantedTitle = normalize(track)
        val wantedArtist = normalize(artist)

        var s = 0
        if (title == wantedTitle) s += 10
        else if (title.contains(wantedTitle) || wantedTitle.contains(title)) s += 5

        if (itemArtist == wantedArtist) s += 10
        else if (itemArtist.contains(wantedArtist) || wantedArtist.contains(itemArtist)) s += 5

        return s
    }

    private fun normalize(value: String): String =
        value.lowercase()
            .replace(Regex("\\(.*?\\)|\\[.*?]"), " ")
            .replace(Regex("\\b(feat|ft|featuring)\\.?\\b.*"), " ")
            .replace(Regex("[^\\p{L}\\p{N}]+"), " ")
            .trim()
            .replace(Regex("\\s+"), " ")
}