package com.github.aqiu202.ideayapi.parser.type;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiType;

import java.util.List;

public interface PsiDescriptor {

    String getName();

    PsiType getType();

    List<PsiModifierListOwner> getElements();

    void addElement(PsiModifierListOwner element);

    void addElement(int index, PsiModifierListOwner element);

    boolean isValid();

    String getDescription();

    boolean isDeprecated();

    boolean hasAnnotation(String annotationName);

    List<PsiAnnotation> findAnnotations(String annotationName);

    PsiAnnotation findFirstAnnotation(String annotationName);
}
