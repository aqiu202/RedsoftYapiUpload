package com.github.aqiu202.ideayapi.parser.type;

import com.github.aqiu202.ideayapi.util.PsiUtils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiType;

public class SimpleDescriptorWrapper implements PsiDescriptorWrapper {

    private final PsiType definedType;
    private final PsiClass parentClass;
    private final PsiDescriptor descriptor;
    private PsiType resolvedType;

    public SimpleDescriptorWrapper(PsiType definedType, PsiClass parentClass, PsiDescriptor descriptor) {
        this.definedType = definedType;
        this.parentClass = parentClass;
        this.descriptor = descriptor;
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
        if (this.descriptor == null) {
            return null;
        }
        if (this.resolvedType == null) {
            PsiType targetType = this.descriptor.getType();
            if (this.definedType != null && this.definedType instanceof PsiClassType) {
                targetType = PsiUtils.resolveGenericType(((PsiClassType) this.definedType), targetType);
            }
            if (this.parentClass != null) {
                targetType = PsiUtils.resolveGenericType(this.parentClass, targetType);
            }
            this.resolvedType = targetType;
        }
        return this.resolvedType;
    }

    @Override
    public PsiDescriptor getDescriptor() {
        return descriptor;
    }
}
