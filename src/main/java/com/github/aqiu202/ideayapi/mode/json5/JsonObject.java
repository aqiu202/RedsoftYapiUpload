package com.github.aqiu202.ideayapi.mode.json5;

import com.github.aqiu202.ideayapi.util.StringUtils;

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
        this.preStr();
        String intent = this.intent(this.level);
        StringJoiner joiner = new StringJoiner(",\n", intent + "{\n", "\n" + intent + "}");
        for (JsonItem<?> item : this.value) {
            joiner.add(item.toString());
        }
        return joiner.toString();
    }


    public void addItem(JsonItem<?> item) {
        this.value.add(item);
    }

    @Override
    public String toString(String description) {
        StringJoiner joiner;
        String intent = this.intent(this.level);
        String desc;
        if (commentMode == COMMENT_MODE_SINGLE) {
            desc = StringUtils.isBlank(description) ? "" : "// " + description;
            joiner = new StringJoiner(",\n", "{ " + desc + "\n",
                    "\n" + intent + "}");
        } else {
            desc = StringUtils.isBlank(description) ? "" : "/* " + description + " */";
            joiner = new StringJoiner(",\n", "{ " + desc + "\n",
                    "\n" + intent + "}");
        }
        for (JsonItem<?> jsonItem : this.value) {
            joiner.add(jsonItem.toString());
        }
        return joiner.toString();
    }

    @Override
    protected void preStr() {
        this.value.forEach(i -> {
            i.getValue().setLevel(this.level + 1);
            i.getValue().preStr();
        });
    }
}
