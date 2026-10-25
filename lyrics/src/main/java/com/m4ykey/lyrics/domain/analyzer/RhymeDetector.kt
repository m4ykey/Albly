package com.m4ykey.lyrics.domain.analyzer

import java.text.Normalizer
import kotlin.math.max
import kotlin.math.min

object RhymeDetector {

    private const val NEAR_THRESHOLD = 0.7
    private const val VOWELS =
        "aeyiou" + "аеёиоуыэюяіїє" + "αεηιουω"
    private const val MIN_WORD_LENGTH = 3
    private const val MIN_ENDING_LENGTH = 2
    private const val MAX_LINE_DISTANCE = 4
    private val WORD_REGEX = Regex("""[\p{L}\p{N}]+(?:['’][\p{L}\p{N}]+)*""")
    private val DIACRITICS = Regex("\\p{Mn}+")

    fun analyze(lyrics : String) : List<RhymeGroup> {
        val tokens = extractTokens(lyrics.lines())

        val groups = mutableListOf<RhymeGroup>()
        val assigned = BooleanArray(tokens.size)
        var nextGroupId = 0

        for (i in tokens.indices) {
            if (assigned[i]) continue

            val first = tokens[i]
            val matches = ArrayList<RhymeMatch>()

            for (j in i + 1 until tokens.size) {
                val candidate = tokens[j]

                if (candidate.lineIndex - first.lineIndex > MAX_LINE_DISTANCE) break

                if (assigned[j]) continue

                if (candidate.normalized == first.normalized) continue

                val exact = first.rhymeEnding == candidate.rhymeEnding
                val sim = if (exact) 1.0 else similarity(first.rhymeEnding, candidate.rhymeEnding)

                if (!exact && sim < NEAR_THRESHOLD) continue

                if (matches.isEmpty()) {
                    matches += first.toMatch(nextGroupId, RhymeType.EXACT, 1.0)
                }

                matches += candidate.toMatch(
                    groupId = nextGroupId,
                    type = if (exact) RhymeType.EXACT else RhymeType.NEAR,
                    similarity = sim
                )
                assigned[j] = true
            }

            if (matches.size >= 2) {
                assigned[i] = true

                for (k in i + 1 until tokens.size) {
                    if (tokens[k].lineIndex - first.lineIndex > MAX_LINE_DISTANCE) break
                    if (!assigned[k] && tokens[k].normalized == first.normalized) {
                        matches += tokens[k].toMatch(nextGroupId, RhymeType.EXACT, 1.0)
                        assigned[k] = true
                    }
                }

                groups += RhymeGroup(id = nextGroupId, matches = matches)
                nextGroupId++
            }
        }

        return groups
    }

    private fun WordToken.toMatch(groupId : Int, type : RhymeType, similarity : Double) =
        RhymeMatch(
            lineIndex = lineIndex,
            word = word,
            type = type,
            similarity = similarity,
            groupId = groupId,
            wordEnd = wordEnd,
            wordStart = wordStart
        )

    private fun extractTokens(lines : List<String>) : List<WordToken> {
        val result = ArrayList<WordToken>(lines.size)

        lines.forEachIndexed { lineIndex, line ->
            for (m in WORD_REGEX.findAll(line)) {
                val word = m.value
                val normalized = normalize(word)

                if (normalized.length < MIN_WORD_LENGTH) continue

                val ending = getRhymeEnding(normalized) ?: continue
                if (ending.length < MIN_ENDING_LENGTH) continue

                result += WordToken(
                    word = word,
                    lineIndex = lineIndex,
                    normalized = normalized,
                    rhymeEnding = ending,
                    wordStart = m.range.first,
                    wordEnd = m.range.last + 1
                )
            }
        }

        return result
    }

    private fun normalize(word : String) : String {
        val lower = word.lowercase()
            .replace("ß", "ss")
            .replace("æ", "ae")
            .replace("œ", "oe")
            .replace('ł', 'l')
            .replace('ø', 'o')
            .replace('đ', 'd')
            .replace('ı', 'i')
            .filter { it != '\n' && it != '’' }

        return Normalizer.normalize(lower, Normalizer.Form.NFD)
            .replace(DIACRITICS, "")
    }

    private fun getRhymeEnding(word : String) : String? {
        var last = -1
        var beforeLast = -1

        for (i in word.indices) {
            if (word[i] in VOWELS) {
                beforeLast = last
                last = i
            }
        }

        val start = when {
            beforeLast >= 0 -> beforeLast
            last >= 0 -> last
            else -> return null
        }

        return word.substring(start)
    }

    private fun similarity(a : String, b: String) : Double {
        if (a == b) return 1.0
        if (a.isEmpty() || b.isEmpty()) return 0.0

        val maxLen = max(a.length, b.length)
        val minLen = min(a.length, b.length)

        if ((maxLen - minLen).toDouble() / maxLen > 1.0 - NEAR_THRESHOLD) return 0.0
        return 1.0 - levenshteinDistance(a, b).toDouble() / maxLen
    }

    private fun levenshteinDistance(a : String, b: String) : Int {
        var previous = IntArray(b.length + 1) { it }
        var current = IntArray(b.length + 1)

        for (i in a.indices) {
            current[0] = i + 1

            for (j in b.indices) {
                val cost = if (a[i] == b[j]) 0 else 1

                current[j + 1] = minOf(
                    current[j] + 1,
                    previous[j + 1] + 1,
                    previous[j] + cost
                )
            }

            val tmp = previous
            previous = current
            current = tmp
        }

        return previous[b.length]
    }
}