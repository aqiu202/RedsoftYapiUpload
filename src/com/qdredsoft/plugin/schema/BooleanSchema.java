package com.qdredsoft.plugin.schema;

import com.qdredsoft.plugin.schema.base.ItemJsonSchema;
import com.qdredsoft.plugin.schema.base.SchemaType;

public final class BooleanSchema extends ItemJsonSchema {

    public BooleanSchema(){
        super(SchemaType.bool);
    }
}
