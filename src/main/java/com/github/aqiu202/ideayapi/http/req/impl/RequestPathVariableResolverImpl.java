package com.github.aqiu202.ideayapi.http.req.impl;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.http.filter.PsiParamFilter;
import com.github.aqiu202.ideayapi.http.req.abs.AbstractRequestParamResolver;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.model.YApiPathVariable;
import com.github.aqiu202.ideayapi.util.DesUtils;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
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
    public void doResolverItem(@NotNull PsiClass targetClass, @NotNull PsiMethod m,
                               @NotNull PsiParameter param, @NotNull YApiParam target) {
        PsiAnnotation annotation = PsiAnnotationUtils
                .findAnnotation(param, SpringMVCConstants.PathVariable);
        if (Objects.nonNull(annotation)) {
            YApiPathVariable pathVariable = new YApiPathVariable();
            pathVariable.full(this.handleParamAnnotation(param, annotation));
            pathVariable.setDesc(DesUtils.getParamDesc(m, param.getName()));
            if (property.isEnableTypeDesc()) {
                pathVariable.setTypeDesc(param.getType().getPresentableText());
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
