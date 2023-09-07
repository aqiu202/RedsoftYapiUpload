package com.github.aqiu202.ideayapi.parser.type;

import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeParameter;

public interface PsiGenericTypeResolver {

    PsiTypeParameter[] findTypeParameters(PsiType psiType);

    PsiSubstitutor getSubstitutor(PsiType psiType);

    PsiType[] resolveTypes(PsiType psiType);

    PsiType resolveType(PsiType psiType, PsiTypeParameter parameter);

    PsiType resolveType(PsiType psiType, int index);

    default PsiType resolveFirst(PsiType psiType) {
        return this.resolveType(psiType, 0);
    }

}
