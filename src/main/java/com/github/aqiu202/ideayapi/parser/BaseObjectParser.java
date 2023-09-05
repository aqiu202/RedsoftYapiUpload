package com.github.aqiu202.ideayapi.parser;

import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.parser.base.LevelCounter;
import com.github.aqiu202.ideayapi.parser.type.PsiFieldWrapper;
import com.github.aqiu202.ideayapi.util.DesUtils;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>接口响应数据解析的抽象提取</b>
 *
 * @author aqiu 2020/7/24 8:23 上午
 **/
public interface BaseObjectParser {

    Jsonable parse(PsiClass rootClass, String typePkName, LevelCounter counter);

    Jsonable parseBasic(String typePkName);

    default Jsonable parseMap(String typePkName) {
        return this.parseMap(typePkName, DesUtils.getTypeDesc("map"));
    }

    Jsonable parseMap(String typePkName, String description);

    default Jsonable parseCollection(PsiClass rootClass, @Nullable String typePkName) {
        return this.parseCollection(rootClass, typePkName, new LevelCounter());
    }

    Jsonable parseCollection(PsiClass rootClass, @Nullable String typePkName, LevelCounter counter);

    //仅支持解析一种泛型
    default Jsonable parsePojo(PsiClass rootClass, String typePkName) {
        return this.parsePojo(rootClass, typePkName, new LevelCounter());
    }

    Jsonable parsePojo(PsiClass rootClass, String typePkName, LevelCounter counter);

    default ValueWrapper parseField(PsiClass targetClass, PsiFieldWrapper field) {
        return parseField(targetClass, field, new LevelCounter());
    }

    ValueWrapper parseField(PsiClass targetClass, PsiFieldWrapper field, LevelCounter counter);
}
