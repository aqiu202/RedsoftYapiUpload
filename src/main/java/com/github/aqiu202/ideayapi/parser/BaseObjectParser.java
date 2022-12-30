package com.github.aqiu202.ideayapi.parser;

import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.parser.base.LevelCounter;
import com.github.aqiu202.ideayapi.util.DesUtils;
import com.intellij.psi.PsiField;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>接口响应数据解析的抽象提取</b>
 *
 * @author aqiu 2020/7/24 8:23 上午
 **/
public interface BaseObjectParser {

    Jsonable parse(String typePkName, LevelCounter counter);

    Jsonable parseBasic(String typePkName);

    default Jsonable parseMap(String typePkName) {
        return this.parseMap(typePkName, DesUtils.getTypeDesc("map"));
    }

    Jsonable parseMap(String typePkName, String description);

    default Jsonable parseCollection(@Nullable String typePkName) {
        return this.parseCollection(typePkName, new LevelCounter());
    }

    Jsonable parseCollection(@Nullable String typePkName, LevelCounter counter);

    //仅支持解析一种泛型
    default Jsonable parsePojo(String typePkName, String genericType) {
        return this.parsePojo(typePkName, genericType, new LevelCounter());
    }

    Jsonable parsePojo(String typePkName, String genericType, LevelCounter counter);

    default ValueWrapper parseField(PsiField field, String genericType) {
        return parseField(field, genericType, new LevelCounter());
    }

    ValueWrapper parseField(PsiField field, String genericType, LevelCounter counter);
}
