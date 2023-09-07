package com.github.aqiu202.ideayapi.http.filter;

import com.github.aqiu202.ideayapi.parser.type.PsiDescriptor;
import com.intellij.psi.PsiClass;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface PsiDescriptorListFilter extends Function<PsiClass, List<PsiDescriptor>> {
}
