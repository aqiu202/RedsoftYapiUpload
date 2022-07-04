package com.github.aqiu202.ideayapi.mode.schema;

import com.github.aqiu202.ideayapi.mode.schema.base.ItemJsonSchema;
import com.github.aqiu202.ideayapi.mode.schema.base.SchemaType;

public final class BooleanSchema extends ItemJsonSchema {

    public BooleanSchema() {
        super(SchemaType.bool);
    }
}
