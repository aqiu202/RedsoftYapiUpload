package com.redsoft.idea.plugin.yapiv2.base.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.base.ApiResolver;
import com.redsoft.idea.plugin.yapiv2.base.PsiMethodParser;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;

public class PsiMethodParserImpl implements PsiMethodParser {

    private final ApiResolver apiResolver = new ApiResolverImpl();

    @Override
    public YApiParam parse(@NotNull PsiClass c, @NotNull PsiMethod m) {
        YApiParam param = new YApiParam();
        apiResolver.resolve(c, m, param);
        return param;
    }
}
