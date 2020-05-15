package com.redsoft.idea.plugin.yapiv2.json5;

import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;

public class JsonObjectWrapper extends Leveler {

//    public JsonObjectWrapper(JsonItem... items) {
//        for (JsonItem item : items) {
//            item.setLevel(this.getLevel() + 1);
//        }
//        this.items = new ArrayList<>(Arrays.asList(items));
//    }
//
//    public JsonObjectWrapper(Collection<JsonItem> items) {
//        items.forEach(i -> i.setLevel(this.getLevel() + 1));
//        this.items = items;
//    }

    private Collection<JsonItem> items;

    public Collection<JsonItem> getItems() {
        return items;
    }

    public void setItems(Collection<JsonItem> items) {
        items.forEach(i -> i.setLevel(this.getLevel() + 1));
        this.items = items;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("", "{\n", this.intent(this.getLevel()) + "}");
        if (Objects.nonNull(this.items)) {
            for (JsonItem item : this.items) {
                joiner.add(item.toString() + "\n");
            }
        }
        return joiner.toString();
    }
}
