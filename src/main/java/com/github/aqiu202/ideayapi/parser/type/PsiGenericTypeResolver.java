package com.github.aqiu202.ideayapi.parser.type;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.PsiType;

public interface PsiGenericTypeResolver {

    PsiSubstitutor getSubstitutor(PsiClassType psiType);

    /**
     * 从包含泛型的类型中解析包含泛型的字段类型
     *
     * @param classType pojo类型
     * @param psiType   字段类型
     */
    PsiType resolveType(PsiClassType classType, PsiType psiType);

    /**
     * 从特定类型中解析方法中包含泛型的参数（方法的入参和出参等）
     *
     * @param rootClass 特定类型
     * @param psiType   参数类型
     * @return 解析后的参数类型
     */
    PsiType resolveType(PsiClass rootClass, PsiType psiType);

    PsiType[] resolveTypes(PsiType psiType);

    PsiType resolveType(PsiType psiType, int index);

    default PsiType resolveFirst(PsiType psiType) {
        return this.resolveType(psiType, 0);
    }

}
