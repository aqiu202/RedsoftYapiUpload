package com.redsoft.idea.plugin.yapiv2.base;

import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.base.FilterableRequestParamResolver;
import com.redsoft.idea.plugin.yapiv2.base.PsiParamListWithBodyFilter;
import com.redsoft.idea.plugin.yapiv2.base.RequestParamResolver;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;

public interface SimpleRequestBodyParamResolver extends
        PsiParamListWithBodyFilter, RequestParamResolver,
        FilterableRequestParamResolver {

    @Override
    default void resolve(@NotNull PsiMethod m, @NotNull YApiParam target) {
        this.doResolve(m, this.filter(m, target), target);
    }

}
