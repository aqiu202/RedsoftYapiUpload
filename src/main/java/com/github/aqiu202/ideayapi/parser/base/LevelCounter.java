package com.github.aqiu202.ideayapi.parser.base;

public final class LevelCounter {
    private int level = 0;

    public void incrementLevel() {
        this.level++;
    }

    public int getLevel() {
        return this.level;
    }
}
