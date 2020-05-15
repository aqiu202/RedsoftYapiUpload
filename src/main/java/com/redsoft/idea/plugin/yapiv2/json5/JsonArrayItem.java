package com.redsoft.idea.plugin.yapiv2.json5;

import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;

public class JsonArrayItem extends JsonItem {

    public JsonArrayItem() {
    }

    //    public JsonArrayItem(String key, JsonArrayWrapper value) {
//        value.setLevel(this.getLevel() + 1);
//        this.key = key;
//        this.value = value;
//    }
//
//    public JsonArrayItem(String key, JsonArrayWrapper value, String description) {
//        value.setLevel(this.getLevel() + 1);
//        this.key = key;
//        this.value = value;
//        this.description = description;
//    }
    public JsonArrayItem(String key, String description) {
        this.key = key;
        this.description = description;
    }

    private String key;
    private JsonArrayWrapper value;
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

    public void setValue(JsonArrayWrapper value) {
        value.setLevel(this.getLevel() + 1);
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
        StringJoiner joiner = new StringJoiner(",\n", "[ //" + this.description + "\n", "\n]");
        Collection<JsonObjectWrapper> wrappers;
        if (Objects.nonNull(this.value) && Objects.nonNull(wrappers = this.value.getWrappers())) {
            for (JsonObjectWrapper wrapper : wrappers) {
                joiner.add("\t" + wrapper.toString());
            }
        }
        return this.intent(this.getLevel()) + "\"" + key + "\":" + joiner.toString();
    }
}
