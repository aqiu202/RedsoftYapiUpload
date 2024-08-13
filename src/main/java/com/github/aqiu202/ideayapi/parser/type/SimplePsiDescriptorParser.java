package com.github.aqiu202.ideayapi.parser.type;

import com.github.aqiu202.ideayapi.util.PsiUtils;
import com.github.aqiu202.ideayapi.util.StringUtils;
import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.psi.*;

public class SimplePsiDescriptorParser implements PsiDescriptorParser {

    public static final SimplePsiDescriptorParser INSTANCE = new SimplePsiDescriptorParser();

    @Override
    public PsiDescriptor parse(PsiField field, PsiType entityType) {
        PsiType psiType = this.resolveGenericType(field.getType(), entityType);
        boolean valid = psiType != null;
        return new SimplePsiDescriptor(field, field.getName(), psiType, valid);
    }

    @Override
    public PsiDescriptor parse(PsiMethod method, PsiType entityType) {
        String methodName = method.getName();
        String prefix;
        PsiType methodType;
        if (methodName.startsWith("set")) {
            prefix = "set";
            PsiParameter firstParameter = method.getParameterList().getParameters()[0];
            methodType = firstParameter == null ? null : firstParameter.getType();
        } else {
            methodType = method.getReturnType();
            prefix = StringUtils.equals("boolean", TypeUtils.getTypePkName(methodType)) ? "is" : "get";
        }
        methodName = methodName.substring(prefix.length());
        String name = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
        PsiType psiType = this.resolveGenericType(methodType, entityType);
        boolean valid = psiType != null;
        return new SimplePsiDescriptor(method, name, psiType, valid);
    }

    protected PsiType resolveGenericType(PsiType propertyType, PsiType entityType) {
        if (propertyType instanceof PsiClassType) {
            PsiClassType classType = (PsiClassType) entityType;
            return PsiUtils.resolveFieldGenericType(classType, propertyType);
        }
        return propertyType;
    }
}
