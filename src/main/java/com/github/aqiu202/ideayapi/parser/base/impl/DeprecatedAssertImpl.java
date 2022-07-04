package com.github.aqiu202.ideayapi.parser.base.impl;

import com.github.aqiu202.ideayapi.parser.base.DeprecatedAssert;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.github.aqiu202.ideayapi.util.PsiDocUtils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.github.aqiu202.ideayapi.constant.DocCommentConstants.TAG_DEPRECATED;

public class DeprecatedAssertImpl implements DeprecatedAssert {
    @Override
    public boolean isDeprecated(@NotNull PsiField c) {
        PsiDocComment classDoc;
        return PsiAnnotationUtils.hasDeprecated(c)
                || (Objects.nonNull(classDoc = c.getDocComment()) &&
                PsiDocUtils.hasTag(classDoc, TAG_DEPRECATED));
    }

    @Override
    public boolean isDeprecated(@NotNull PsiClass c) {
        PsiDocComment classDoc;
        return PsiAnnotationUtils.hasDeprecated(c)
                || (Objects.nonNull(classDoc = c.getDocComment()) &&
                PsiDocUtils.hasTag(classDoc, TAG_DEPRECATED));
    }

    @Override
    public boolean isDeprecated(@NotNull PsiMethod m) {
        PsiDocComment methodDoc;
        return PsiAnnotationUtils.hasDeprecated(m)
                || (Objects.nonNull(methodDoc = m.getDocComment()) &&
                PsiDocUtils.hasTag(methodDoc, TAG_DEPRECATED));
    }

    @Override
    public boolean isDeprecated(@NotNull PsiClass c, @NotNull PsiMethod m) {
        return isDeprecated(c) || isDeprecated(m);
    }
}
