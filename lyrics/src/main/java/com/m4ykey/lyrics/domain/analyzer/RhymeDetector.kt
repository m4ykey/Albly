package com.m4ykey.lyrics.domain.analyzer

import java.text.Normalizer
import kotlin.math.max
import kotlin.math.min

object RhymeDetector {

    private const val NEAR_THRESHOLD = 0.75
    private const val VOWELS = "aeyiou" + "аеёиоуыэюяіїє" + "αεηιουω"
    private const val MIN_WORD_LENGTH = 3
    private const val MIN_ENDING_LENGTH = 2
    private const val MAX_LINE_DISTANCE = 4
    private val WORD_REGEX = Regex("""[\p{L}\p{N}]+(?:['’][\p{L}\p{N}]+)*""")
    private val DIACRITICS = Regex("\\p{Mn}+")
    private const val FREQUENT_MIN_COUNT = 4
    private const val FREQUENT_RATIO = 0.02

    fun analyze(lyrics: String): List<RhymeGroup> {
        val tokens = extractTokens(lyrics.lines())
        val n = tokens.size

        val parent = IntArray(n) { it }
        val bestSim = DoubleArray(n)

        for (i in 0 until n) {
            val a = tokens[i]

            for (j in i + 1 until n) {
                val b = tokens[j]

                if (b.lineIndex - a.lineIndex > MAX_LINE_DISTANCE) break

                val sim = when {
                    a.normalized == b.normalized -> 1.0
                    a.rhymeEnding == b.rhymeEnding -> 1.0
                    else -> similarity(a.rhymeEnding, b.rhymeEnding)
                }

                if (sim < NEAR_THRESHOLD) continue

                union(parent, i, j)
                if (sim > bestSim[i]) bestSim[i] = sim
                if (sim > bestSim[j]) bestSim[j] = sim
            }
        }

        val components = LinkedHashMap<Int, MutableList<Int>>()
        for (i in 0 until n) {
            components.getOrPut(find(parent, i)) { ArrayList() } += i
        }

        val groups = ArrayList<RhymeGroup>()
        var nextGroupId = 0

        for (members in components.values) {
            val distinct = members.map { tokens[it].normalized }.toSet()
            if (distinct.size < 2) continue

            val matches = members.map { idx ->
                val sim = if (bestSim[idx] >= 1.0) 1.0 else bestSim[idx]
                tokens[idx].toMatch(
                    groupId = nextGroupId,
                    type = if (sim >= 1.0) RhymeType.EXACT else RhymeType.NEAR,
                    similarity = sim
                )
            }

            groups += RhymeGroup(id = nextGroupId, matches = matches)
            nextGroupId++
        }

        return groups
    }

    private fun find(parent: IntArray, x: Int): Int {
        var root = x
        while (parent[root] != root) root = parent[root]

        var cur = x
        while (parent[cur] != root) {
            val next = parent[cur]
            parent[cur] = root
            cur = next
        }
        return root
    }

    private fun union(parent: IntArray, a: Int, b: Int) {
        val ra = find(parent, a)
        val rb = find(parent, b)
        if (ra != rb) parent[maxOf(ra, rb)] = minOf(ra, rb)
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

    private fun extractTokens(lines: List<String>): List<WordToken> {
        class Raw(val lineIndex: Int, val word: String, val normalized: String, val start: Int, val end: Int)

        val raw = ArrayList<Raw>()
        lines.forEachIndexed { lineIndex, line ->
            for (m in WORD_REGEX.findAll(line)) {
                raw += Raw(
                    lineIndex = lineIndex,
                    word = m.value,
                    normalized = normalize(m.value),
                    start = m.range.first,
                    end = m.range.last + 1
                )
            }
        }

        val counts = HashMap<String, Int>()
        for (r in raw) counts[r.normalized] = (counts[r.normalized] ?: 0) + 1

        val frequentThreshold = max(FREQUENT_MIN_COUNT, (raw.size * FREQUENT_RATIO).toInt())

        val result = ArrayList<WordToken>(raw.size)
        for (r in raw) {
            if (r.normalized.length < MIN_WORD_LENGTH) continue

            if (r.normalized.length == 3 && r.normalized.last() in VOWELS) continue

            if ((counts[r.normalized] ?: 0) >= frequentThreshold) continue

            val ending = getRhymeEnding(r.normalized) ?: continue
            if (ending.length < MIN_ENDING_LENGTH) continue

            result += WordToken(
                word = r.word,
                lineIndex = r.lineIndex,
                normalized = r.normalized,
                rhymeEnding = ending,
                wordStart = r.start,
                wordEnd = r.end
            )
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
            .filter { it != '\'' && it != '’' }

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