package com.redsoft.idea.plugin.yapiv2.model;

/**
 * header
 *
 * @date 2019/5/9 10:11 PM
 */
@SuppressWarnings("unused")
public class YApiHeader extends ValueWrapper {

    public YApiHeader() {
    }

    public YApiHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 值
     */
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
