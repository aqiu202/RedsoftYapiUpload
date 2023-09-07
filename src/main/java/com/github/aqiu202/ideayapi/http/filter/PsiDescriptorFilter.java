package com.github.aqiu202.ideayapi.http.filter;

import com.github.aqiu202.ideayapi.parser.type.PsiDescriptor;
import com.intellij.psi.PsiClass;

import java.util.function.BiFunction;

@FunctionalInterface
public interface PsiDescriptorFilter extends BiFunction<PsiDescriptor, PsiClass, Boolean> {
}
