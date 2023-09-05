package com.github.aqiu202.ideayapi.parser.type;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;

public interface PsiFieldWrapper {
    PsiField getField();

    PsiClass getParentClass();

    PsiType resolveFieldType();
}
