package com.dongtronic.keeptalking

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import java.util.ArrayList
import java.util.stream.Collectors

internal class PageChooserTest {
    @Test
    fun testNoDuplicatePages() {
        val password = "Hello"
        val playerCount = 5
        val pageCount = 24
        val chooser = PageChooser(playerCount, pageCount, password)
        val result = chooser.choose()

        val generatedPages = result.values.stream().flatMap { it.stream() }.collect(Collectors.toSet())

        Assertions.assertEquals(pageCount, generatedPages.size)
    }
}