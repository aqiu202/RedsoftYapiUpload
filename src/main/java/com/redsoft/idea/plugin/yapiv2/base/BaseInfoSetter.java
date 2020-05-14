package com.redsoft.idea.plugin.yapiv2.base;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;

public interface BaseInfoSetter {

    void set(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target);
}
