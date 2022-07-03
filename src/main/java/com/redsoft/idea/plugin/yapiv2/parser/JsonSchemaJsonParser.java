package com.redsoft.idea.plugin.yapiv2.parser;

import com.redsoft.idea.plugin.yapiv2.schema.base.ItemJsonSchema;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>json-schema解析器</b>
 * @author aqiu 2020/7/24 9:22 上午
 **/
public interface JsonSchemaJsonParser extends ObjectJsonParser {

    ItemJsonSchema parseJsonSchema(String typePkName, List<String> ignores);

    default ItemJsonSchema parseJsonSchema(String typePkName) {
        return this.parseJsonSchema(typePkName, new ArrayList<>());
    }

}
