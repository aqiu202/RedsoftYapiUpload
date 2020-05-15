package com.redsoft.idea.plugin.yapiv2.req;

import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;

public interface RequestResolver {

    void resolve(@NotNull PsiMethod m, @NotNull YApiParam target);
}
