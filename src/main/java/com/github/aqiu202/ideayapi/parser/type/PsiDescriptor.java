package com.github.aqiu202.ideayapi.parser.type;

import com.github.aqiu202.ideayapi.util.CollectionUtils;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiType;

import java.util.List;

public interface PsiDescriptor {

    String getName();

    PsiType getType();

    List<PsiModifierListOwner> getElements();

    void addElement(PsiModifierListOwner element);

    default PsiModifierListOwner getFirstElement() {
        List<PsiModifierListOwner> elements = this.getElements();
        if (CollectionUtils.isEmpty(elements)) {
            return null;
        }
        return elements.get(0);
    }

    boolean isValid();

    String getDescription();

    boolean isDeprecated();

    boolean hasAnnotation(String annotationName);
    List<PsiAnnotation> findAnnotations(String annotationName);
    PsiAnnotation findFirstAnnotation(String annotationName);
}
