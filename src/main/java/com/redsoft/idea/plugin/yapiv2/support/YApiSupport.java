package com.redsoft.idea.plugin.yapiv2.support;

import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.model.ValueWrapper;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;

/**
 * Yapi接口信息的扩展支持
 */
public interface YApiSupport {

    default int getOrder() {
        return 0;
    }

    void handleMethod(PsiMethod psiMethod, YApiParam apiDTO);

    void handleParam(ValueWrapper wrapper);

    void handleField(ValueWrapper wrapper);
}
