package com.redsoft.idea.plugin.yapiv2.parser;

import com.redsoft.idea.plugin.yapiv2.schema.base.ItemJsonSchema;

public interface JsonSchemaJsonParser extends ObjectJsonParser {

    ItemJsonSchema parseJsonSchema(String typePkName);
    
}
