package com.github.aqiu202.ideayapi.http.filter;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface PsiFieldListFilter extends Function<PsiClass, List<PsiField>> {
}
