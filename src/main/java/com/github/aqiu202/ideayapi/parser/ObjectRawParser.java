package com.github.aqiu202.ideayapi.parser;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;

/**
 * <b>raw格式的接口响应数据解析</b>
 *
 * @author aqiu 2020/7/24 8:22 上午
 **/
public interface ObjectRawParser extends BaseObjectParser {

    String getRawResponse(PsiClass rootClass, PsiType psiType);
}
