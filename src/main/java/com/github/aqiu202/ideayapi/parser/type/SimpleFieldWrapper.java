package com.github.aqiu202.ideayapi.parser.type;

import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;

public class SimpleFieldWrapper implements PsiFieldWrapper {

    private final PsiClass parentClass;
    private final PsiField field;
    private PsiType resolvedType;

    public SimpleFieldWrapper(PsiClass parentClass, PsiField field) {
        this.parentClass = parentClass;
        this.field = field;
    }

    @Override
    public PsiClass getParentClass() {
        return parentClass;
    }

    @Override
    public PsiType resolveFieldType() {
        if (this.parentClass == null || this.field == null) {
            return null;
        }
        if (this.resolvedType == null) {
            this.resolvedType = TypeUtils.resolveGenericType(this.parentClass, this.field.getType());
        }
        return this.resolvedType;
    }

    @Override
    public PsiField getField() {
        return field;
    }
}
