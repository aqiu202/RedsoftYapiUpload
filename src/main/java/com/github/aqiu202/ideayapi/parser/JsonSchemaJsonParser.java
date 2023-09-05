package com.github.aqiu202.ideayapi.parser;

import com.github.aqiu202.ideayapi.mode.schema.base.ItemJsonSchema;
import com.github.aqiu202.ideayapi.parser.base.LevelCounter;
import com.intellij.psi.PsiClass;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>json-schema解析器</b>
 *
 * @author aqiu 2020/7/24 9:22 上午
 **/
public interface JsonSchemaJsonParser extends ObjectJsonParser {

    ItemJsonSchema parseJsonSchema(PsiClass rootClass, String typePkName, LevelCounter counter);

    default ItemJsonSchema parseJsonSchema(PsiClass rootClass, String typePkName) {
        return this.parseJsonSchema(rootClass, typePkName, new LevelCounter());
    }

}
