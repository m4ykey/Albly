package com.m4ykey.lyrics.domain.analyzer

import java.text.Normalizer
import kotlin.math.max

object RhymeDetector {

    fun analyze(lyrics : String) : List<RhymeGroup> {
        val endings = lyrics
            .lines()
            .mapIndexedNotNull { index, line ->
                val word = extractLastWord(line) ?: return@mapIndexedNotNull null
                val normalized = normalize(word)

                if (normalized.length < 2) return@mapIndexedNotNull null

                LineEnding(
                    lineIndex = index,
                    originalWord = word,
                    normalizedWord = normalized,
                    rhymeEnding = getRhymeEnding(normalized)
                )
            }

        val groups = mutableListOf<RhymeGroup>()
        val assigned = mutableSetOf<Int>()
        var nextGroupId = 0

        for (i in endings.indices) {
            if (i in assigned) continue

            val first = endings[i]
            val matches = mutableListOf<RhymeMatch>()

            for (j in i + 1 until endings.size) {
                if (j in assigned) continue

                val candidate = endings[j]

                val similarity = similarity(
                    first.rhymeEnding,
                    candidate.rhymeEnding
                )

                val exact = first.rhymeEnding == candidate.rhymeEnding
                val near = similarity >= 0.65

                if (exact || near) {
                    if (matches.isEmpty()) {
                        matches += RhymeMatch(
                            lineIndex = first.lineIndex,
                            word = first.originalWord,
                            groupId = nextGroupId,
                            type = RhymeType.EXACT,
                            similarity = 1.0
                        )
                    }

                    matches += RhymeMatch(
                        lineIndex = candidate.lineIndex,
                        word = candidate.originalWord,
                        groupId = nextGroupId,
                        type = if (exact) RhymeType.EXACT else RhymeType.NEAR,
                        similarity = similarity
                    )

                    assigned += j
                }
            }

            if (matches.size >= 2) {
                assigned += i
                groups += RhymeGroup(
                    id = nextGroupId,
                    matches = matches
                )
                nextGroupId++
            }
        }

        return groups
    }

    private fun extractLastWord(line : String) : String? {
        return Regex("""[\p{L}\p{N}]+(?:['’][\p{L}\p{N}]+)*""")
            .findAll(line)
            .lastOrNull()
            ?.value
    }

    private fun normalize(word : String) : String {
        return Normalizer.normalize(word.lowercase(), Normalizer.Form.NFC)
            .replace('ó', 'u')
            .replace('ę', 'e')
            .replace('ą', 'a')
            .replace('ć', 'c')
            .replace('ł', 'l')
            .replace('ń', 'n')
            .replace('ś', 's')
            .replace('ź', 'z')
            .replace('ż', 'z')
    }

    private fun getRhymeEnding(word : String) : String {
        val vowels = "aeyiou"
        val index = word.indexOfLast { it in vowels }

        return if (index >= 0) word.substring(index) else word
    }

    private fun similarity(a : String, b: String) : Double {
        if (a == b) return 1.0
        if (a.isEmpty() || b.isEmpty()) return 0.0

        val distance = levenshteinDistance(a, b)
        return 1.0 - distance.toDouble() / max(a.length, b.length)
    }

    private fun levenshteinDistance(a : String, b: String) : Int {
        var previous = IntArray(b.length + 1) { it }

        for (i in a.indices) {
            val current = IntArray(b.length + 1)
            current[0] = i + 1

            for (j in b.indices) {
                val substitutionCost = if (a[i] == b[j]) 0 else 1

                current[j + 1] = minOf(
                    current[j] + 1,
                    previous[j + 1] + 1,
                    previous[j] + substitutionCost
                )
            }

            previous = current
        }

        return previous[b.length]
    }
}