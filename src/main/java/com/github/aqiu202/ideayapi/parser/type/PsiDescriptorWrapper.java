package com.github.aqiu202.ideayapi.parser.type;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;

public interface PsiDescriptorWrapper {
    PsiDescriptor getDescriptor();

    PsiType getDefinedType();

    PsiClass getParentClass();

    PsiType resolveFieldType();
}
