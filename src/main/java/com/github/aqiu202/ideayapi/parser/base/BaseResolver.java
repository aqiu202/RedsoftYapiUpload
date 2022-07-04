package com.github.aqiu202.ideayapi.parser.base;

import com.github.aqiu202.ideayapi.model.YApiParam;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

public interface BaseResolver {

    void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target);
}
