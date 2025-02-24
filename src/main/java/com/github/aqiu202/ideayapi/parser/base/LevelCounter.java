package com.github.aqiu202.ideayapi.parser.base;

import java.util.HashMap;
import java.util.Map;

public final class LevelCounter {

    private final Map<String, Integer> levelMap = new HashMap<String, Integer>();

    public void incrementLevel(String rawTypeName) {
        this.levelMap.compute(rawTypeName, (key, value) -> {
            if (value == null) {
                return 1;
            } else {
                return value + 1;
            }
        });
    }

    public int getLevel(String rawTypeName) {
        return this.levelMap.getOrDefault(rawTypeName, 0);
    }
}
