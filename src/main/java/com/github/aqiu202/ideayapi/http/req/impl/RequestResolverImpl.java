package com.github.aqiu202.ideayapi.http.req.impl;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.http.req.RequestParamResolver;
import com.github.aqiu202.ideayapi.http.req.RequestResolver;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class RequestResolverImpl implements RequestResolver {

    private final List<RequestParamResolver> paramResolvers;

    public RequestResolverImpl(YApiProjectProperty property, Project project) {
        RequestParamResolver queryResolver = new RequestQueryResolverImpl(property, project);
        RequestParamResolver bodyResolver = new RequestBodyResolverImpl(property, project);
        RequestParamResolver headerResolver = new RequestHeaderResolverImpl();
        RequestParamResolver pathVariableResolver = new RequestPathVariableResolverImpl(property);
        RequestParamResolver formResolver = new RequestFormResolverImpl(property, project);
        paramResolvers = Arrays.asList(pathVariableResolver, formResolver, queryResolver,
                bodyResolver, headerResolver);
    }

    @Override
    public void resolve(@NotNull PsiClass targetClass, @NotNull PsiMethod m, @NotNull YApiParam target) {
        this.paramResolvers.forEach(r -> r.resolve(targetClass, m, target));
    }

}
