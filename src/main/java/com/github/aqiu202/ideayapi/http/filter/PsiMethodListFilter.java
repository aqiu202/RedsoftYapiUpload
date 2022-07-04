package com.github.aqiu202.ideayapi.http.filter;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface PsiMethodListFilter extends Function<PsiClass, List<PsiMethod>> {
}
