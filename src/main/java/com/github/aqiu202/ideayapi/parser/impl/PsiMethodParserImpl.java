package com.github.aqiu202.ideayapi.parser.impl;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.PsiMethodParser;
import com.github.aqiu202.ideayapi.parser.api.ApiParser;
import com.github.aqiu202.ideayapi.parser.api.impl.ApiResolverImpl;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

public class PsiMethodParserImpl implements PsiMethodParser {

    private final ApiParser apiResolver;

    public PsiMethodParserImpl(YApiProjectProperty property, Project project) {
        this.apiResolver = new ApiResolverImpl(property, project);
    }

    public PsiMethodParserImpl(ApiParser apiResolver) {
        this.apiResolver = apiResolver;
    }

    @Override
    public YApiParam parse(@NotNull PsiClass c, @NotNull PsiMethod m) {
        return this.apiResolver.parse(c, m);
    }
}
