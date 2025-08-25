package com.github.aqiu202.ideayapi.mode.json5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.StringJoiner;

public class JsonArray<T> extends Json<Collection<Json<T>>> {

    @SafeVarargs
    public JsonArray(Json<T>... items) {
        this.value = new ArrayList<>(Arrays.asList(items));
    }

    public JsonArray(Collection<Json<T>> items) {
        this.value = items;
    }

    @Override
    public String toString() {
        return this.toString(0);
    }

    @Override
    public String toString(String description, int level, CommentMode commentMode) {
        String intent = this.intent(level);
        String desc = this.buildCommentString(description, commentMode);
        StringJoiner joiner = new StringJoiner(",\n",
                intent + "[ " + desc + "\n",
                "\n" + intent + "]");
        int subLevel = level + 1;
        String subIntent = this.intent(subLevel);
        for (Json<T> json : this.value) {
            joiner.add(subIntent + json.toString(subLevel, commentMode));
        }
        return joiner.toString();
    }

}
