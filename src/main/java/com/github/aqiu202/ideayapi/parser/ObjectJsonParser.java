package com.github.aqiu202.ideayapi.parser;

import com.github.aqiu202.ideayapi.parser.type.DescriptorResolver;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;

/**
 * <b>json格式的接口响应数据解析</b>
 *
 * @author aqiu 2020/7/24 8:21 上午
 **/
public interface ObjectJsonParser extends BaseObjectParser {

    String getJson(PsiClass rootClass, PsiType psiType);

    DescriptorResolver getDescriptorResolver();

}
