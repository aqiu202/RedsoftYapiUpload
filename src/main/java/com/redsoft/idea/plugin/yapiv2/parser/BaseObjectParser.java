package com.redsoft.idea.plugin.yapiv2.parser;

import com.intellij.psi.PsiField;
import com.redsoft.idea.plugin.yapiv2.model.FieldValueWrapper;
import org.jetbrains.annotations.Nullable;

/**
 * <b>接口响应数据解析的抽象提取</b>
 * @author aqiu
 * @date 2020/7/24 8:23 上午
 **/
public interface BaseObjectParser {

    Jsonable parse(String typePkName);

    Jsonable parseBasic(String typePkName);

    Jsonable parseMap(String typePkName);

    Jsonable parseCollection(@Nullable String typePkName);

    //仅支持解析一种泛型
    Jsonable parsePojo(String typePkName, String genericType);

    FieldValueWrapper parseField(PsiField field, String genericType);
}
