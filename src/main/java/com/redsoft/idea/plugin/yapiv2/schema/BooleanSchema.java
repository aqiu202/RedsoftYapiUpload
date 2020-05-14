package com.redsoft.idea.plugin.yapiv2.schema;

import com.redsoft.idea.plugin.yapiv2.schema.base.ItemJsonSchema;
import com.redsoft.idea.plugin.yapiv2.schema.base.SchemaType;

public final class BooleanSchema extends ItemJsonSchema {

    public BooleanSchema() {
        super(SchemaType.bool);
    }
}
