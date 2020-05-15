package com.redsoft.idea.plugin.yapiv2.json5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;

public class JsonItemWrapper {

    public JsonItemWrapper(JsonItem... items) {
        this.items = new ArrayList<>(Arrays.asList(items));
    }

    public JsonItemWrapper(Collection<JsonItem> items) {
        this.items = items;
    }

    private Collection<JsonItem> items;

    public Collection<JsonItem> getItems() {
        return items;
    }

    public void setItems(Collection<JsonItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("", "{", "}");
        if (Objects.nonNull(this.items)) {
            for (JsonItem item : this.items) {
                joiner.add(item.toString());
            }
        }
        return joiner.toString();
    }
}
