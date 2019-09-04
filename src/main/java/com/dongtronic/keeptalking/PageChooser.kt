package com.dongtronic.keeptalking

import java.util.*
import kotlin.random.Random

class PageChooser(var playerCount: Int = 0, var pages: Int = 0, var introPages: Int = 0, var random: Boolean = false, var skipIntroPages: Boolean = true, var seedString: String = "") {
    private var result: HashMap<Int, ArrayList<Int>>? = null
    private var playerRandom: Random = Random(0)

    fun choose(): Map<Int, ArrayList<Int>> {
        val usedPages = ArrayList<Int>()
        val random = Random(seedString.hashCode())
        playerRandom = Random(seedString.hashCode())

        // Prepare player list
        val players = HashMap<Int, ArrayList<Int>>()
        (1..playerCount).forEach { i ->
            players[i] = ArrayList()
        }

        // Choose pages
        var player = 1

        if (!skipIntroPages) {
            introPages = 0
        }

        repeat((introPages until pages).count()) {
            var page: Int
            do {
                page = random.nextInt(pages - introPages) + 1 + introPages
            } while (usedPages.contains(page))

            usedPages.add(page)

            players[player]!!.add(page)
            player = if (this.random) {
                incrementPlayerRandom(playerCount)
            } else {
                incrementPlayer(player, playerCount)
            }
        }

        result = players

        result!!.forEach { (index, _) -> result!![index]!!.sort() }

        return Collections.unmodifiableMap(players)
    }

    private fun incrementPlayer(current: Int, total: Int): Int {
        var next = current + 1
        if (next > total) {
            next = 1
        }

        return next
    }

    private fun incrementPlayerRandom(total: Int): Int {
        return playerRandom.nextInt(total) + 1
    }

    fun validatePlayerCount() {
        val errorMessage = "You cannot have more players than %s pages"
        if (skipIntroPages) {
            require( (pages - introPages) >= playerCount ) { String.format(errorMessage, pages - introPages) }
        } else {
            require(pages >= playerCount) { String.format(errorMessage, pages) }
        }
    }
}
