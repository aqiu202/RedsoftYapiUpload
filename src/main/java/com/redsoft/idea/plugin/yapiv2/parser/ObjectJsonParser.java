package com.redsoft.idea.plugin.yapiv2.parser;

import com.intellij.psi.PsiType;

/**
 * <b>json格式的接口响应数据解析</b>
 * @author aqiu 2020/7/24 8:21 上午
 **/
public interface ObjectJsonParser extends BaseObjectParser {

    String getJsonResponse(PsiType psiType);

}
