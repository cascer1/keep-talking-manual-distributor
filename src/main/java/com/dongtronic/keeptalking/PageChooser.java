package com.dongtronic.keeptalking;

import java.util.*;

public class PageChooser {
    private int playerCount;
    private int pages;
    private String seedString = "";
    private Long seed = 0L;
    private HashMap<Integer, ArrayList<Integer>> result;
    private Random playerRandom = new Random();

    public PageChooser(int playerCount, int pages, String seedString) {
        this.playerCount = playerCount;
        this.pages = pages;
        this.seedString = seedString;
    }

    public static void main(String[] args) {
        PageChooser chooser = new PageChooser(4, 23, "Hello, world!");
        Map result = chooser.choose();
        chooser.printResult();
    }

    /**
     * Turn the seed String into a Long so it can be used in Random object creation
     */
    private void calculateSeed() {
        long result = 0L;
        byte[] bytes = seedString.getBytes();

        for (byte aByte : bytes) {
            result += (int) aByte;
        }

        this.seed = result;
    }

    public Map<Integer, ArrayList<Integer>> choose() {
        ArrayList<Integer> usedPages = new ArrayList<>();
        calculateSeed();
        Random random = new Random(seed);

        // Prepare player list
        HashMap<Integer, ArrayList<Integer>> players = new HashMap<>();
        for (int i = 1; i <= playerCount; i++) {
            players.put(i, new ArrayList<>());
        }

        // Choose pages
        int player = 1;
        for (int i = 0; i < pages; i++) {
            int page = 0;
            do {
                page = random.nextInt(pages) + 1;
            } while (usedPages.contains(page));

            usedPages.add(page);

            players.get(player).add(page);
            player = incrementPlayer(player, playerCount);
        }

        result = players;
        return Collections.unmodifiableMap(players);
    }

    private int incrementPlayer(int current, int total) {
        int next = current + 1;
        if (next > total) {
            next = 1;
        }

        return next;
    }

    // in the future we may add support for giving each player a random amount of pages
    private int incrementPlayerRandom(int current, int total) {
        return playerRandom.nextInt(total) + 1;
    }

    public void printResult() {
        result.forEach((player, pages) -> System.out.println("Player " + player + " will receive pages: " + pages.toString()));
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getSeedString() {
        return seedString;
    }

    public void setSeedString(String seedString) {
        this.seedString = seedString;
    }
}
