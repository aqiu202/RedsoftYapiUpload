package com.github.aqiu202.ideayapi.parser;

import com.github.aqiu202.ideayapi.mode.json5.Json;
import com.github.aqiu202.ideayapi.parser.base.LevelCounter;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;

/**
 * <b>json5解析器</b>
 *
 * @author aqiu 2020/7/24 9:23 上午
 **/
public interface Json5JsonParser extends ObjectJsonParser {

    default Json<?> parseJson5(PsiClass rootClass, PsiType type) {
        return this.parseJson5(rootClass, type, new LevelCounter());
    }

    Json<?> parseJson5(PsiClass rootClass, PsiType type, LevelCounter counter);

}
