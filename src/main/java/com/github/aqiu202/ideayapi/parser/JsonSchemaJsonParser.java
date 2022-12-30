package com.github.aqiu202.ideayapi.parser;

import com.github.aqiu202.ideayapi.mode.schema.base.ItemJsonSchema;
import com.github.aqiu202.ideayapi.parser.base.LevelCounter;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>json-schema解析器</b>
 *
 * @author aqiu 2020/7/24 9:22 上午
 **/
public interface JsonSchemaJsonParser extends ObjectJsonParser {

    ItemJsonSchema parseJsonSchema(String typePkName, LevelCounter counter);

    default ItemJsonSchema parseJsonSchema(String typePkName) {
        return this.parseJsonSchema(typePkName, new LevelCounter());
    }

}
