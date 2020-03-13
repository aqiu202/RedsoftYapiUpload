package com.redsoft.idea.plugin.yapi.schema.base;

@SuppressWarnings("unused")
public enum SchemaType {
    string("string"),
    number("number"),
    array("array"),
    object("object"),
    integer("integer"),
    bool("boolean");

    private String name;

    SchemaType(String name) {
        this.name = name;
    }

    public static SchemaType parse(String name) {
        switch (name) {
            case "number":
                return number;
            case "array":
                return array;
            case "object":
                return object;
            case "integer":
                return integer;
            case "boolean":
                return bool;
            default:
                return string;
        }
    }

    @Override
    public String toString(){
        return this.name;
    }
}
