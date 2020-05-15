package com.redsoft.idea.plugin.yapiv2.json5;

import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;

public class JsonObjectItem extends JsonItem {

    public JsonObjectItem() {
    }

    public JsonObjectItem(String key) {
        this.key = key;
    }

    public JsonObjectItem(String key, JsonObjectWrapper value) {
        this.key = key;
        this.value = value;
    }

    public JsonObjectItem(String key, JsonObjectWrapper value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

    private String key;
    private JsonObjectWrapper value;
    private String description;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(JsonObjectWrapper value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",\n", "{ //" + this.description +"\n", "\n}");
        Collection<JsonItem> items;
        if (Objects.nonNull(this.value) && Objects.nonNull(items = this.value.getItems())) {
            for (JsonItem item : items) {
                joiner.add("\t" + item.toString());
            }
        }
        return this.intent(this.getLevel()) + "\"" + key + "\":" + joiner.toString();
    }

}
