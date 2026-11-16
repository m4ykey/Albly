package com.m4ykey.core

import com.m4ykey.core.database.decode
import com.m4ykey.core.database.encode
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import org.junit.Test
import kotlin.test.assertEquals

class JsonConverterTest {

    @Serializable
    private data class Sample(
        val id : Int,
        val name : String,
        val label : String? = null
    )

    @Test
    fun `list survives round trip`() {
        val items = listOf(Sample(1, "a"), Sample(2, "b", "c"))
        assertEquals(items, decode<List<Sample>>(encode(items)))
    }

    @Test
    fun `empty list survives round trip`() {
        assertEquals(emptyList<Sample>(), decode<List<Sample>>(encode(emptyList<Sample>())))
    }

    @Test
    fun `special characters survive round trip`() {
        val items = listOf(Sample(1, "Beyoncé \"Queen B\" / Tiësto, Łukasz & 日本語"))

        assertEquals(items, decode<List<Sample>>(encode(items)))
    }

    @Test
    fun `unknown keys are ignored`() {
        val old = """[{"id":1,"name":"a","removedField":"x"}]"""

        assertEquals(listOf(Sample(1, "a")), decode<List<Sample>>(old))
    }

    @Test
    fun `missing field with default uses default`() {
        val old = """[{"id":1,"name":"a"}]"""

        assertEquals(listOf(Sample(1, "a", null)), decode<List<Sample>>(old))
    }

    @Test(expected = SerializationException::class)
    fun `malformed json throws`() {
        decode<List<Sample>>("not json")
    }
}