package com.github.aqiu202.ideayapi.parser.support;

import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.type.PsiDescriptor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierListOwner;

import java.util.List;

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

    default void handleProperty(ValueWrapper wrapper) {
    }

    default boolean isIgnored(PsiDescriptor descriptor, PsiClass psiClass) {
        List<PsiModifierListOwner> elements = descriptor.getElements();
        boolean ignored = false;
        for (PsiModifierListOwner element : elements) {
            if (element instanceof PsiField) {
                ignored = ignored || this.isIgnored(((PsiField) element), psiClass);
            }
            if (element instanceof PsiMethod) {
                ignored = ignored || this.isIgnored(((PsiMethod) element), psiClass);
            }
        }
        return ignored;
    }

    default boolean isIgnored(PsiField field, PsiClass psiClass) {
        return false;
    }

    default boolean isIgnored(PsiMethod method, PsiClass psiClass) {
        return false;
    }

}
