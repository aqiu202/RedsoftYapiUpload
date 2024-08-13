package com.github.aqiu202.ideayapi.parser.type;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;

public interface PsiDescriptorParser {

    PsiDescriptor parse(PsiField field, PsiType entityType);

    PsiDescriptor parse(PsiMethod method, PsiType entityType);

}
