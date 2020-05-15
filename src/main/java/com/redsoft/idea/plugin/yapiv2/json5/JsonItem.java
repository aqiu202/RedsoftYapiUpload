package com.redsoft.idea.plugin.yapiv2.json5;

public class JsonItem extends Leveler {

    public JsonItem() {
    }

    public JsonItem(String key) {
        this.key = key;
    }

    public JsonItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public JsonItem(String key, String value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

    private String key;
    private String value;
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

    public void setValue(String value) {
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
        return this.intent(this.getLevel()) + "\"" + this.key + "\":" + this.value + ",//"
                + this.description;
    }

}
