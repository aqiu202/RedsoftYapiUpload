package com.github.aqiu202.ideayapi.mode.json5;

public class JsonItem<T> {

    public JsonItem(String key) {
        this(key, null);
    }

    public JsonItem(String key, Json<T> value) {
        this.key = key;
        this.value = value;
    }

    public JsonItem(String key, Json<T> value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

    private String key;
    private Json<T> value;
    private String description;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Json<T> getValue() {
        return value;
    }

    public void setValue(Json<T> value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString(int level, CommentMode commentMode) {
        String intent = this.intent(level);
        StringBuilder builder = new StringBuilder(intent);
        builder.append("\"").append(this.key)
                .append("\":").append(this.value.toString(this.description, level, commentMode));
        return builder.toString();
    }

    private String intent(int level) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; i++) {
            builder.append(Json.INTENT);
        }
        return builder.toString();
    }
}
