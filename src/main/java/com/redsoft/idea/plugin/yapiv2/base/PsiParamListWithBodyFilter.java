package com.redsoft.idea.plugin.yapiv2.base;

import com.intellij.psi.PsiParameter;
import com.redsoft.idea.plugin.yapiv2.base.PsiParamListFilter;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.util.PsiParamUtils;
import org.jetbrains.annotations.NotNull;

public interface PsiParamListWithBodyFilter extends PsiParamListFilter {

    default boolean hasRequestBody(PsiParameter[] psiParameters) {
        return PsiParamUtils.hasRequestBody(psiParameters);
    }

    default boolean noBody(@NotNull YApiParam target) {
        return PsiParamUtils.noBody(target.getMethod());
    }
}
