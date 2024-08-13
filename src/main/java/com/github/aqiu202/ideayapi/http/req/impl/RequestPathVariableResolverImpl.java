package com.github.aqiu202.ideayapi.http.req.impl;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.http.filter.PsiParamFilter;
import com.github.aqiu202.ideayapi.http.req.abs.AbstractRequestParamResolver;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.model.YApiPathVariable;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.github.aqiu202.ideayapi.util.PsiDocUtils;
import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class RequestPathVariableResolverImpl extends AbstractRequestParamResolver {

    private final YApiProjectProperty property;

    public RequestPathVariableResolverImpl(YApiProjectProperty property) {
        this.property = property;
    }

    @NotNull
    @Override
    public PsiParamFilter getPsiParamFilter(@NotNull PsiMethod m,
                                            @NotNull YApiParam target) {
        return p -> PsiAnnotationUtils
                .isAnnotatedWith(p, SpringMVCConstants.PathVariable);
    }

    @Override
    public void doResolverItem(@NotNull PsiClass rootClass, @NotNull PsiMethod m,
                               @NotNull PsiParameter param, PsiType paramType, @NotNull YApiParam target) {
        PsiAnnotation annotation = PsiAnnotationUtils
                .findAnnotation(param, SpringMVCConstants.PathVariable);
        if (Objects.nonNull(annotation)) {
            YApiPathVariable pathVariable = new YApiPathVariable();
            pathVariable.full(this.handleParamAnnotation(param, annotation));
            pathVariable.setDesc(PsiDocUtils.getParamComment(m, param.getName()));
            if (property.isEnableTypeDesc()) {
                pathVariable.setTypeDesc(TypeUtils.getTypeName(param.getType()));
            }
            Set<YApiPathVariable> pathVariables = target.getReq_params();
            if (Objects.isNull(pathVariables)) {
                pathVariables = new LinkedHashSet<>();
                target.setReq_params(pathVariables);
            }
            pathVariables.add(pathVariable);
        }
    }

}
