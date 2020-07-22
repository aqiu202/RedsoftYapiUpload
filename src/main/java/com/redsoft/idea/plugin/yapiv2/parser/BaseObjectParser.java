package com.redsoft.idea.plugin.yapiv2.parser;

import org.jetbrains.annotations.Nullable;

public interface BaseObjectParser {

    Jsonable parse(String typePkName);

    Jsonable parseBasic(String typePkName);

    Jsonable parseMap(String typePkName);

    Jsonable parseCollection(@Nullable String typePkName);

    Jsonable parsePojo(String typePkName);

    //仅支持解析一种泛型
    Jsonable parsePojo(String typePkName, String subType);
}
