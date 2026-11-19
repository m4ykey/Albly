package com.m4ykey.lyrics.data.service

import com.m4ykey.lyrics.data.dto.LyricsDtoItem
import com.m4ykey.lyrics.data.dto.LyricsRedDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

class LyricsService(
    private val lrcLibClient: HttpClient,
    private val redLyricsClient: HttpClient
) : RemoteLyricsService {

    override suspend fun searchLyrics(
        trackName: String,
        artistName : String,
        albumName : String?
    ): List<LyricsDtoItem> {
        return try {
            lrcLibClient.get("search") {
                parameter("track_name", trackName)
                parameter("artist_name", artistName)
                parameter("album_name", albumName)
            }.body()
        } catch (e : Exception) {
            throw e
        }
    }

    override suspend fun getLyrics(id: Int): LyricsDtoItem {
        return lrcLibClient.get("get/$id").body()
    }

    override suspend fun searchRedLyrics(q: String): LyricsRedDto {
        return redLyricsClient.get("search.json") {
            parameter("q", q)
        }.body()
    }

    override suspend fun getRedLyrics(isrc: String): String {
        return extractLyricsText(redLyricsClient.get("s/$isrc.ttml").bodyAsText())
    }

    private fun extractLyricsText(ttml : String) : String {
        return PARAGRAPH.findAll(ttml)
            .map { match ->
                decodeEntities(
                    TAG.replace(BREAK.replace(match.groupValues[1], "\n"), "")
                ).trim()
            }
            .filter { it.isNotEmpty() }
            .joinToString("\n")
    }

    private fun decodeEntities(value : String) : String {
        return ENTITY.replace(value) { match ->
            when (val entity = match.groupValues[1]) {
                "amp" -> "&"
                "lt" -> "<"
                "gt" -> ">"
                "quot" -> "\""
                "apos" -> "'"
                "nbsp" -> " "
                else -> when {
                    entity.startsWith("#x", ignoreCase = true) -> {
                        entity.drop(2).toIntOrNull(16)?.let(::Char)?.toString() ?: match.value
                    }
                    entity.startsWith("#") -> {
                        entity.drop(1).toIntOrNull()?.let(::Char)?.toString() ?: match.value
                    }

                    else -> match.value
                }
            }
        }
    }

    private companion object {
        val PARAGRAPH = Regex(
            "<p\\b[^>]*>(.*?)</p>",
            setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
        )
        val BREAK = Regex("<br\\s*/?>", RegexOption.IGNORE_CASE)
        val TAG = Regex("<[^>]+>")
        val ENTITY = Regex("&(#x?[0-9a-fA-F]+|[a-zA-Z]+);")
    }
}