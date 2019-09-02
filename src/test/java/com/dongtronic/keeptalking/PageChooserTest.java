package com.dongtronic.keeptalking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class PageChooserTest {
    @Test
    void testNoDuplicatePages() {
        String password = "Hello";
        int playerCount = 5;
        int pageCount = 24;
        PageChooser chooser = new PageChooser(playerCount, pageCount, password);
        Map<Integer, ArrayList<Integer>> result = chooser.choose();

        Set<Integer> generatedPages = result.values().stream()
                                            .flatMap(Collection::stream)
                                            .collect(Collectors.toSet());

        Assertions.assertEquals(pageCount, generatedPages.size());
    }
}