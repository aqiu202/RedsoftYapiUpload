package com.redsoft.idea.plugin.yapiv2.parser;

import com.redsoft.idea.plugin.yapiv2.json5.Json;

public interface Json5JsonParser extends ObjectJsonParser {

    Json<?> parseJson5(String typePkName);

}
