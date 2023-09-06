package com.github.aqiu202.ideayapi.parser;

import com.github.aqiu202.ideayapi.mode.schema.base.ItemJsonSchema;
import com.github.aqiu202.ideayapi.parser.base.LevelCounter;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;

/**
 * <b>json-schema解析器</b>
 *
 * @author aqiu 2020/7/24 9:22 上午
 **/
public interface JsonSchemaJsonParser extends ObjectJsonParser {

    ItemJsonSchema parseJsonSchema(PsiClass rootClass, PsiType type, LevelCounter counter);

    default ItemJsonSchema parseJsonSchema(PsiClass rootClass, PsiType type) {
        return this.parseJsonSchema(rootClass, type, new LevelCounter());
    }

}
