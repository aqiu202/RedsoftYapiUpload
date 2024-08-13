package com.github.aqiu202.ideayapi.parser.type;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.PsiType;

public class SimplePsiGenericTypeResolver implements PsiGenericTypeResolver {

    public static final PsiGenericTypeResolver INSTANCE = new SimplePsiGenericTypeResolver();

    @Override
    public PsiSubstitutor getSubstitutor(PsiClassType classType) {
        PsiClassType.ClassResolveResult resolveResult = classType.resolveGenerics();
        if (resolveResult.isValidResult()) {
            return resolveResult.getSubstitutor();
        }
        return null;
    }

    @Override
    public PsiType resolveType(PsiClassType classType, PsiType psiType) {
        PsiSubstitutor substitutor = this.getSubstitutor(classType);
        if (substitutor != null) {
            return substitutor.substitute(psiType);
        }
        return psiType;
    }

    @Override
    public PsiType resolveType(PsiClass rootClass, PsiType psiType) {
        PsiType result = psiType;
        if (psiType instanceof PsiClassType) {
            PsiClassType[] superTypes = rootClass.getSuperTypes();
            for (PsiClassType superType : superTypes) {
                result = resolveType(superType, result);
//            if (!StringUtils.equals(type.getCanonicalText(), psiType.getCanonicalText())) {
//                return type;
//            }
            }
        }
        return result;
    }

    @Override
    public PsiType[] resolveTypes(PsiType psiType) {
        if (psiType instanceof PsiClassType) {
            return ((PsiClassType) psiType).getParameters();
        }
//        PsiTypeParameter[] genericTypeParams = this.findTypeParameters(psiType);
//        if (genericTypeParams == null) {
//            return null;
//        }
//        PsiSubstitutor substitutor = this.getSubstitutor(psiType);
//        if (substitutor == null) {
//            return null;
//        }
//        PsiType[] result = new PsiType[genericTypeParams.length];
//        for (int i = 0; i < genericTypeParams.length; i++) {
//            PsiTypeParameter genericTypeParam = genericTypeParams[i];
//            result[i] = substitutor.substitute(genericTypeParam);
//        }
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
