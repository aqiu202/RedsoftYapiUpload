package com.github.aqiu202.ideayapi.parser.type;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiType;

public interface PsiDescriptor {

    String getName();

    PsiType getType();

    PsiModifierListOwner getOrigin();

    PsiClass getParent();

    boolean isValid();
}
