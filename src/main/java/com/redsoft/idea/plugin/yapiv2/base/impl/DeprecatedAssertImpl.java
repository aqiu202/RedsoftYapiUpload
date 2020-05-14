package com.redsoft.idea.plugin.yapiv2.base.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.redsoft.idea.plugin.yapiv2.base.DeprecatedAssert;
import com.redsoft.idea.plugin.yapiv2.constant.AnnotationConstants;
import com.redsoft.idea.plugin.yapiv2.util.PsiAnnotationUtils;
import com.redsoft.idea.plugin.yapiv2.util.PsiDocUtils;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class DeprecatedAssertImpl implements DeprecatedAssert {

    @Override
    public boolean isDeprecated(@NotNull PsiClass c) {
        PsiDocComment classDoc;
        return PsiAnnotationUtils.hasDeprecated(c)
                || (Objects.nonNull(classDoc = c.getDocComment()) &&
                PsiDocUtils.hasTag(classDoc, AnnotationConstants.DEPRECATED_VALUE));
    }

    @Override
    public boolean isDeprecated(@NotNull PsiMethod m) {
        PsiDocComment methodDoc;
        return PsiAnnotationUtils.hasDeprecated(m)
                || (Objects.nonNull(methodDoc = m.getDocComment()) &&
                PsiDocUtils.hasTag(methodDoc, AnnotationConstants.DEPRECATED_VALUE));
    }

    @Override
    public boolean isDeprecated(@NotNull PsiClass c, @NotNull PsiMethod m) {
        return isDeprecated(c) || isDeprecated(m);
    }
}
