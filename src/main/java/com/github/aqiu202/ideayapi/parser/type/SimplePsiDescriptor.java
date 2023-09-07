package com.github.aqiu202.ideayapi.parser.type;

import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.psi.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class SimplePsiDescriptor implements PsiDescriptor {
    private final PsiClass parent;
    private final PsiModifierListOwner origin;
    private final String name;
    private final PsiType type;
    private final boolean valid;

    public SimplePsiDescriptor(PsiModifierListOwner origin) {
        this.origin = origin;
        this.parent = null;
        this.name = null;
        this.type = null;
        this.valid = false;
    }

    public static SimplePsiDescriptor of(PsiModifierListOwner origin) {
        if (origin instanceof PsiField) {
            return new SimplePsiDescriptor(((PsiField) origin));
        }
        if (origin instanceof PsiMethod) {
            return new SimplePsiDescriptor(((PsiMethod) origin));
        }
        return new SimplePsiDescriptor(origin);
    }

    public SimplePsiDescriptor(PsiField origin) {
        this.origin = origin;
        this.parent = ((PsiClass) origin.getParent());
        this.name = origin.getName();
        this.type = origin.getType();
        this.valid = true;
    }

    public SimplePsiDescriptor(PsiMethod origin) {
        this.origin = origin;
        this.parent = ((PsiClass) origin.getParent());
        this.type = origin.getReturnType();
        String prefix = StringUtils.equals("boolean", TypeUtils.getTypePkName(type)) ? "is" : "get";
        String name = origin.getName();
        this.valid = !StringUtils.equals("getClass", name)
                && name.startsWith(prefix) && origin.getParameterList().isEmpty();
        if (this.valid) {
            name = name.substring(prefix.length());
            this.name = name.substring(0,1).toLowerCase() + name.substring(1);
        } else {
            this.name = name;
        }
    }

    @Override
    public PsiModifierListOwner getOrigin() {
        return origin;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PsiType getType() {
        return type;
    }

    @Override
    public PsiClass getParent() {
        return parent;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplePsiDescriptor that = (SimplePsiDescriptor) o;
        return valid == that.valid && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, valid);
    }
}
