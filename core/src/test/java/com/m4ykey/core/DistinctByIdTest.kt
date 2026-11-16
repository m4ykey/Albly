package com.m4ykey.core

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.m4ykey.core.paging.distinctById
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DistinctByIdTest {

    private data class Item(
        val id : Int?,
        val name : String
    )

    private suspend fun List<Item>.duplication() : List<Item> =
        flowOf(PagingData.from(this).distinctById { it.id }).asSnapshot()

    @Test
    fun `removes duplicates and keeps first occurrence`() = runTest {
        val result = listOf(
            Item(1, "first"),
            Item(2, "b"),
            Item(1, "duplicate"),
        ).duplication()

        assertEquals(listOf(Item(1, "first"), Item(2, "b")), result)

    }

    @Test
    fun `keeps all items when ids are unique`() = runTest {
        val items = listOf(Item(1, "a"), Item(2, "b"), Item(3, "c"))

        assertEquals(items, items.duplication())
    }

    @Test
    fun `keeps every item with null id`() = runTest {
        val items = listOf(Item(null, "a"), Item(null, "b"), Item(null, "c"))

        assertEquals(items, items.duplication())
    }

    @Test
    fun `empty list stays empty`() = runTest {
        assertEquals(emptyList<Item>(), emptyList<Item>().duplication())
    }
}