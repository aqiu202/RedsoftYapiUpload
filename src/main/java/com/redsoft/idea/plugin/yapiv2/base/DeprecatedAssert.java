package com.redsoft.idea.plugin.yapiv2.base;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

public interface DeprecatedAssert {

    boolean isDeprecated(@NotNull PsiClass c);

    boolean isDeprecated(@NotNull PsiMethod c);

    boolean isDeprecated(@NotNull PsiClass c, @NotNull PsiMethod m);
}
