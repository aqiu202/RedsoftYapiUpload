package com.redsoft.idea.plugin.yapiv2.req;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface FilterableRequestParamResolver extends RequestParamResolver, PsiParamListFilter {

    default void doResolve(@NotNull PsiMethod m,
            @NotNull List<PsiParameter> parameterList,
            @NotNull YApiParam target) {
        parameterList.forEach(p -> this.doResolverItem(m, p, target));
    }

    default void doResolverItem(@NotNull PsiMethod m, @NotNull PsiParameter param,
            @NotNull YApiParam target) {

    }
}
