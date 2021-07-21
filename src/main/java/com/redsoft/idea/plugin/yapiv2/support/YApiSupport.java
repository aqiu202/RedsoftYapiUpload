package com.redsoft.idea.plugin.yapiv2.support;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.redsoft.idea.plugin.yapiv2.model.ValueWrapper;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;

public interface YApiSupport {

    default boolean isImportant() {
        return false;
    }

    default int getOrder() {
        return 0;
    }

    void handleMethod(PsiMethod psiMethod, YApiParam apiDTO);

    void handleParam(ValueWrapper wrapper);

    void handleField(ValueWrapper wrapper);
}
