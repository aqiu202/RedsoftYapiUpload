package com.github.aqiu202.ideayapi.parser.type;

import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;

public class SimpleFieldWrapper implements PsiFieldWrapper {

    private final PsiType definedType;
    private final PsiClass parentClass;
    private final PsiField field;
    private PsiType resolvedType;

    public SimpleFieldWrapper(PsiType definedType, PsiClass parentClass, PsiField field) {
        this.definedType = definedType;
        this.parentClass = parentClass;
        this.field = field;
    }

    @Override
    public PsiType getDefinedType() {
        return definedType;
    }

    @Override
    public PsiClass getParentClass() {
        return parentClass;
    }

    @Override
    public PsiType resolveFieldType() {
        if (this.field == null) {
            return null;
        }
        if (this.resolvedType == null) {
            PsiType targetType = this.field.getType();
            if (this.definedType != null && this.definedType instanceof PsiClassType) {
                targetType = TypeUtils.resolveGenericType(((PsiClassType) this.definedType), targetType);
            }
            if (this.parentClass != null) {
                targetType = TypeUtils.resolveGenericType(this.parentClass, targetType);
            }
            this.resolvedType = targetType;
        }
        return this.resolvedType;
    }

    @Override
    public PsiField getField() {
        return field;
    }
}
