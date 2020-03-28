package com.redsoft.idea.plugin.yapi.support;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.redsoft.idea.plugin.yapi.model.ValueWrapper;
import com.redsoft.idea.plugin.yapi.model.YApiDTO;

public interface YApiSupport {

    default boolean isImportant() {
        return false;
    }
    default int getOrder() {
        return 0;
    }
    void handleMethod(PsiMethod psiMethod, YApiDTO apiDTO);
    void handleParam(PsiParameter psiParameter, ValueWrapper wrapper);
    void handleField(PsiField psiField, ValueWrapper wrapper);
}
