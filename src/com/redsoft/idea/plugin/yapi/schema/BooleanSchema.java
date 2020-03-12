package com.redsoft.idea.plugin.yapi.schema;

import com.redsoft.idea.plugin.yapi.schema.base.ItemJsonSchema;
import com.redsoft.idea.plugin.yapi.schema.base.SchemaType;

public final class BooleanSchema extends ItemJsonSchema {

    public BooleanSchema(){
        super(SchemaType.bool);
    }
}
