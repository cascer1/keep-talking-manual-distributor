package com.dongtronic.keeptalking

import java.util.*

class PageChooser(var playerCount: Int = 0, var pages: Int = 0, var introPages: Int = 0, var random: Boolean = false, var skipIntroPages: Boolean = true, var seedString: String = "") {
    private var seed: Long? = 0L
    private var result: HashMap<Int, ArrayList<Int>>? = null
    private var playerRandom: Random = Random()

    /**
     * Turn the seed String into a Long so it can be used in Random object creation
     */
    private fun calculateSeed() {
        var result = 0L
        val bytes = seedString.toByteArray()

        for (aByte in bytes) {
            result += aByte.toInt().toLong()
        }

        this.seed = result
    }

    fun choose(): Map<Int, ArrayList<Int>> {
        val usedPages = ArrayList<Int>()
        calculateSeed()
        val random = Random(seed!!)
        playerRandom = Random(seed!!)

        // Prepare player list
        val players = HashMap<Int, ArrayList<Int>>()
        for (i in 1..playerCount) {
            players[i] = ArrayList()
        }

        // Choose pages
        var player = 1

        if (!skipIntroPages) {
            introPages = 0
        }

        for (i in introPages until pages) {
            var page = 0
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
        return Collections.unmodifiableMap(players)
    }

    private fun incrementPlayer(current: Int, total: Int): Int {
        var next = current + 1
        if (next > total) {
            next = 1
        }

        return next
    }

    // in the future we may add support for giving each player a random amount of pages
    private fun incrementPlayerRandom(total: Int): Int {
        return playerRandom.nextInt(total) + 1
    }
}
