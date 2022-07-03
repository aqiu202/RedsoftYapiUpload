package com.redsoft.idea.plugin.yapiv2.parser;

import com.intellij.psi.PsiField;
import com.redsoft.idea.plugin.yapiv2.model.FieldValueWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>接口响应数据解析的抽象提取</b>
 * @author aqiu 2020/7/24 8:23 上午
 **/
public interface BaseObjectParser {

    Jsonable parse(String typePkName, List<String> ignores);

    Jsonable parseBasic(String typePkName);

    default Jsonable parseMap(String typePkName) {
        return this.parseMap(typePkName, "map");
    }

    Jsonable parseMap(String typePkName, String description);

    default Jsonable parseCollection(@Nullable String typePkName) {
        return this.parseCollection(typePkName, new ArrayList<>());
    }

    Jsonable parseCollection(@Nullable String typePkName, List<String> ignores);

    //仅支持解析一种泛型
    default Jsonable parsePojo(String typePkName, String genericType) {
        return this.parsePojo(typePkName, genericType, new ArrayList<>());
    }

    Jsonable parsePojo(String typePkName, String genericType, List<String> ignores);

    default FieldValueWrapper parseField(PsiField field, String genericType) {
        return parseField(field, genericType, new ArrayList<>());
    }

    FieldValueWrapper parseField(PsiField field, String genericType, List<String> ignores);
}
