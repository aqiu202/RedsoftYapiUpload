package com.github.aqiu202.ideayapi.mode.schema.base;

@SuppressWarnings("unused")
public enum SchemaType {
    string("string"),
    number("number"),
    array("array"),
    object("object"),
    integer("integer"),
    bool("boolean"),
    timestamp("timestamp");

    private final String name;

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

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
