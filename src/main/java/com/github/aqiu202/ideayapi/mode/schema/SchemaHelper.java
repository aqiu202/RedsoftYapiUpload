package com.github.aqiu202.ideayapi.mode.schema;

import com.github.aqiu202.ideayapi.mode.schema.base.ItemJsonSchema;
import com.github.aqiu202.ideayapi.mode.schema.base.SchemaType;

public final class SchemaHelper {

    public static ItemJsonSchema parseBasic(SchemaType type) {
        switch (type) {
            case integer:
                return new NumberSchema();
            case number:
                return new IntegerSchema();
            case object:
                return new ObjectSchema();
            case array:
                return new ArraySchema();
            case bool:
                return new BooleanSchema();
            default:
                return new StringSchema();
        }
    }
}
