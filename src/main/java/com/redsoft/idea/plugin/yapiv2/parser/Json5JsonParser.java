package com.redsoft.idea.plugin.yapiv2.parser;

import com.redsoft.idea.plugin.yapiv2.json5.Json;

/**
 * <b>json5解析器</b>
 * @author aqiu 2020/7/24 9:23 上午
 **/
public interface Json5JsonParser extends ObjectJsonParser {

    Json<?> parseJson5(String typePkName);

}
