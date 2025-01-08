package com.github.aqiu202.ideayapi.parser;

import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.parser.base.LevelCounter;
import com.github.aqiu202.ideayapi.parser.type.PsiDescriptor;
import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;

/**
 * <b>接口响应数据解析的抽象提取</b>
 *
 * @author aqiu 2020/7/24 8:23 上午
 **/
public interface BaseObjectParser {

    Jsonable parse(PsiClass rootClass, PsiType type, LevelCounter counter);

    Jsonable parseBasic(PsiType psiType);

    default Jsonable parseMap(PsiClass rootClass, PsiType type) {
        return this.parseMap(rootClass, type, TypeUtils.getTypeDesc("map"));
    }

    Jsonable parseMap(PsiClass rootClass, PsiType type, String description);

    default Jsonable parseCollection(PsiClass rootClass, PsiType type) {
        return this.parseCollection(rootClass, type, new LevelCounter());
    }

    Jsonable parseCollection(PsiClass rootClass, PsiType type, LevelCounter counter);

    //支持解析泛型
    default Jsonable parsePojo(PsiClass rootClass, PsiType psiType) {
        return this.parsePojo(rootClass, psiType, new LevelCounter());
    }

    Jsonable parsePojo(PsiClass rootClass, PsiType psiType, LevelCounter counter);

    default ValueWrapper parseProperty(PsiClass rootClass, PsiDescriptor field) {
        return parseProperty(rootClass, field, new LevelCounter());
    }

    ValueWrapper parseProperty(PsiClass rootClass, PsiDescriptor field, LevelCounter counter);
}
