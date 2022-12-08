package com.github.aqiu202.ideayapi.parser.support;

import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;

/**
 * Yapi接口信息的扩展支持
 */
public interface YApiSupport {

    default int getOrder() {
        return 0;
    }

    default void handleMenu(PsiClass psiClass, YApiParam apiParam) {

    }

    default void handleMethod(PsiMethod psiMethod, YApiParam apiDTO) {
    }

    default void handleParam(ValueWrapper wrapper) {
    }

    default void handleField(ValueWrapper wrapper) {
    }

    default boolean isIgnored(PsiField field, PsiClass psiClass) {
        return false;
    }
}
