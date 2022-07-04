package com.github.aqiu202.ideayapi.http.filter;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import java.util.function.BiFunction;

@FunctionalInterface
public interface PsiFieldFilter extends BiFunction<PsiField, PsiClass, Boolean> {
}
