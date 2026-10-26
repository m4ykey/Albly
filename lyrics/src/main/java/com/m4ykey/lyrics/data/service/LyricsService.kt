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
        val response = redLyricsClient.get("s/$isrc.ttml")

        response.bodyAsText().let { ttml ->
            return extractLyricsText(ttml)
        }
    }

    private fun extractLyricsText(ttml : String) : String {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(
            InputSource(StringReader(ttml))
        )

        val paragraphs = document.getElementsByTagName("p")

        return (0 until paragraphs.length).joinToString("\n") { index ->
            paragraphs.item(index).textContent.trim()
        }
    }
}