package com.redsoft.idea.plugin.yapiv2.json5;

import java.util.Arrays;

public class Leveler {

    private int level;

    protected int getLevel() {
        return level;
    }

    protected void setLevel(int level) {
        this.level = level;
    }

    protected String intent(int level) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; i++) {
            builder.append("\t");
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        JsonObjectWrapper map = new JsonObjectWrapper();
        map.setItems(Arrays.asList(new JsonItem("key1", "value1", "哈哈"),
                new JsonItem("key2", "value2", "哈哈哈哈")));
        JsonArrayWrapper wrapper = new JsonArrayWrapper();
        wrapper.setWrappers(Arrays.asList(map, map));
        JsonArrayItem jsonArrayItem = new JsonArrayItem("array", "呵呵呵呵呵");
        jsonArrayItem.setValue(wrapper);
        System.out.println(jsonArrayItem);
    }
}
