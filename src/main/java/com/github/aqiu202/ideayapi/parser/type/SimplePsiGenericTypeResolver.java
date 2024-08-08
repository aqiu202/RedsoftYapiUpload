package com.github.aqiu202.ideayapi.parser.type;

import com.intellij.psi.*;

public class SimplePsiGenericTypeResolver implements PsiGenericTypeResolver {

    public static final PsiGenericTypeResolver INSTANCE = new SimplePsiGenericTypeResolver();

    @Override
    public PsiTypeParameter[] findTypeParameters(PsiType psiType) {
        if (psiType instanceof PsiClassType) {
            PsiClass psiClass = ((PsiClassType) psiType).resolve();
            if (psiClass != null) {
                return psiClass.getTypeParameters();
            }
        }
        return null;
    }

    @Override
    public PsiSubstitutor getSubstitutor(PsiType psiType) {
        if (psiType instanceof PsiClassType) {
            PsiClassType.ClassResolveResult resolveResult = ((PsiClassType) psiType).resolveGenerics();
            if (resolveResult.isValidResult()) {
                return resolveResult.getSubstitutor();
            }
        }
        return null;
    }

    @Override
    public PsiType[] resolveTypes(PsiType psiType) {
        PsiTypeParameter[] genericTypeParams = this.findTypeParameters(psiType);
        if (genericTypeParams == null) {
            return null;
        }
        PsiSubstitutor substitutor = this.getSubstitutor(psiType);
        if (substitutor == null) {
            return null;
        }
        PsiType[] result = new PsiType[genericTypeParams.length];
        for (int i = 0; i < genericTypeParams.length; i++) {
            PsiTypeParameter genericTypeParam = genericTypeParams[i];
            result[i] = substitutor.substitute(genericTypeParam);
        }
        return result;
    }

    @Override
    public PsiType resolveType(PsiType psiType, PsiTypeParameter parameter) {
        PsiSubstitutor substitutor = this.getSubstitutor(psiType);
        if (substitutor != null) {
            return substitutor.substitute(parameter);
        }
        return null;
    }

    @Override
    public PsiType resolveType(PsiType psiType, int index) {
        PsiType[] psiTypes = this.resolveTypes(psiType);
        if (psiTypes == null || psiTypes.length <= index) {
            return null;
        }
        return psiTypes[index];
    }
}
