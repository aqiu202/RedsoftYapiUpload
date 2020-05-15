package com.redsoft.idea.plugin.yapiv2.req;

import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;

public interface SimpleRequestParamResolver extends PsiParamListFilter,
        RequestParamResolver,
        FilterableRequestParamResolver {

    @Override
    default void resolve(@NotNull PsiMethod m, @NotNull YApiParam target) {
        this.doResolve(m, this.filter(m, target), target);
    }
}
