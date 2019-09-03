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
        val chooser = PageChooser(playerCount, pageCount,0, false, false, password)
        val result = chooser.choose()

        val generatedPages = result.values.stream().flatMap { it.stream() }.collect(Collectors.toSet())

        Assertions.assertEquals(pageCount, generatedPages.size)
    }

    @Test
    fun testNoDuplicatePagesSkipIntro() {
        val password = "Hello"
        val playerCount = 3
        val pageCount = 24
        val introPageCount = 5
        val chooser = PageChooser(playerCount, pageCount,introPageCount, false, true, password)
        val result = chooser.choose()

        val generatedPages = result.values.stream().flatMap { it.stream() }.collect(Collectors.toSet())

        Assertions.assertEquals(pageCount - introPageCount, generatedPages.size)
    }

    @Test
    fun testSkipIntro() {
        val password = "Hello"
        val playerCount = 3
        val pageCount = 24
        val introPageCount = 5
        val chooser = PageChooser(playerCount, pageCount,introPageCount, false, true, password)
        val result = chooser.choose()

        val generatedPages = result.values.stream().flatMap { it.stream() }.collect(Collectors.toSet())

        (1 .. introPageCount).forEach{ page ->
            Assertions.assertFalse(generatedPages.contains(page))
        }
    }

    @Test
    fun testSkipIntroRandom() {
        val password = "Hello"
        val playerCount = 3
        val pageCount = 24
        val introPageCount = 5
        val chooser = PageChooser(playerCount, pageCount,introPageCount, true, true, password)
        val result = chooser.choose()

        val generatedPages = result.values.stream().flatMap { it.stream() }.collect(Collectors.toSet())

        (1 .. introPageCount).forEach{ page ->
            Assertions.assertFalse(generatedPages.contains(page))
        }

        Assertions.assertEquals(pageCount - introPageCount, generatedPages.size)
    }

    @Test
    fun testRandom() {
        val password = "Hello"
        val playerCount = 3
        val pageCount = 24
        val introPageCount = 5
        val chooser = PageChooser(playerCount, pageCount,introPageCount, true, false, password)
        val result = chooser.choose()

        val generatedPages = result.values.stream().flatMap { it.stream() }.collect(Collectors.toSet())

        Assertions.assertEquals(pageCount, generatedPages.size)
    }
}