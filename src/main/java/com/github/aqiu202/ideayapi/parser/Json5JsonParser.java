package com.github.aqiu202.ideayapi.parser;

import com.github.aqiu202.ideayapi.mode.json5.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>json5解析器</b>
 *
 * @author aqiu 2020/7/24 9:23 上午
 **/
public interface Json5JsonParser extends ObjectJsonParser {

    default Json<?> parseJson5(String typePkName) {
        return this.parseJson5(typePkName, new ArrayList<>());
    }

    Json<?> parseJson5(String typePkName, List<String> ignores);

}
