package com.redsoft.idea.plugin.yapiv2.json5;

import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;

public class JsonArrayWrapper extends Leveler {

//    public JsonArrayWrapper(JsonObjectWrapper... wrappers) {
//        for (JsonObjectWrapper wrapper : wrappers) {
//            wrapper.setLevel(this.getLevel() + 1);
//        }
//        this.wrappers = new ArrayList<>(Arrays.asList(wrappers));
//    }
//    public JsonArrayWrapper(Collection<JsonObjectWrapper> wrappers) {
//        wrappers.forEach(w -> w.setLevel(this.getLevel() + 1));
//        this.wrappers = wrappers;
//    }

    private Collection<JsonObjectWrapper> wrappers;

    private String description;

    public Collection<JsonObjectWrapper> getWrappers() {
        return wrappers;
    }

    public void setWrappers(
            Collection<JsonObjectWrapper> wrappers) {
        wrappers.forEach(w -> w.setLevel(this.getLevel() + 1));
        this.wrappers = wrappers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",\n", "[\n", "\n]");
        if (Objects.nonNull(this.wrappers)) {
            for (JsonObjectWrapper wrapper : wrappers) {
                joiner.add(wrapper.toString());
            }
        }
        return joiner.toString();
    }

}
