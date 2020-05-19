package com.redsoft.idea.plugin.yapiv2.parser.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.api.ApiResolver;
import com.redsoft.idea.plugin.yapiv2.api.impl.ApiResolverImpl;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.parser.PsiMethodParser;
import com.redsoft.idea.plugin.yapiv2.xml.YApiProjectProperty;
import org.jetbrains.annotations.NotNull;

public class PsiMethodParserImpl implements PsiMethodParser {

    private final ApiResolver apiResolver;

    public PsiMethodParserImpl(YApiProjectProperty property, Project project) {
        apiResolver = new ApiResolverImpl(property, project);
    }

    @Override
    public YApiParam parse(@NotNull PsiClass c, @NotNull PsiMethod m) {
        YApiParam param = new YApiParam();
        apiResolver.resolve(c, m, param);
        return param;
    }
}
