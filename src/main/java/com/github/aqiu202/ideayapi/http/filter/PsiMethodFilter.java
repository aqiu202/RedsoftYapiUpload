package com.github.aqiu202.ideayapi.http.filter;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.function.BiFunction;

@FunctionalInterface
public interface PsiMethodFilter extends BiFunction<PsiMethod, PsiClass, Boolean> {
}
