package com.redsoft.idea.plugin.yapiv2.parser;

import com.intellij.psi.PsiType;

/**
 * <b>raw格式的接口响应数据解析</b>
 * @author aqiu
 * @date 2020/7/24 8:22 上午
 **/
public interface ObjectRawParser extends BaseObjectParser {

    String getRawResponse(PsiType psiType);
}
