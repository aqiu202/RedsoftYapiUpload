package com.github.aqiu202.ideayapi.mode.json5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.StringJoiner;

public class JsonObject extends Json<Collection<JsonItem<?>>> {

    public JsonObject(JsonItem<?>... value) {
        this.value = new ArrayList<>(Arrays.asList(value));
    }

    public JsonObject(Collection<JsonItem<?>> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.toString(0);
    }

    public void addItem(JsonItem<?> item) {
        this.value.add(item);
    }

    @Override
    public String toString(String description, int level, CommentMode commentMode) {
        String intent = this.intent(level);
        String desc = this.buildCommentString(description, commentMode);
        StringJoiner joiner = new StringJoiner(",\n", "{ " + desc + "\n",
                "\n" + intent + "}");
        for (JsonItem<?> jsonItem : this.value) {
            joiner.add(jsonItem.toString(level + 1, commentMode));
        }
        return joiner.toString();
    }

}
