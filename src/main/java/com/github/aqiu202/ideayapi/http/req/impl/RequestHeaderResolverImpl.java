package com.github.aqiu202.ideayapi.http.req.impl;

import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.http.filter.PsiParamFilter;
import com.github.aqiu202.ideayapi.http.req.abs.AbstractRequestParamResolver;
import com.github.aqiu202.ideayapi.model.YApiHeader;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class RequestHeaderResolverImpl extends AbstractRequestParamResolver {

    private final PsiParamFilter psiParamFilter = (param) ->
            PsiAnnotationUtils.isAnnotatedWith(param, SpringMVCConstants.RequestHeader);

    @NotNull
    @Override
    public PsiParamFilter getPsiParamFilter(@NotNull PsiMethod m,
                                            @NotNull YApiParam target) {
        return this.psiParamFilter;
    }

    @Override
    public void doResolverItem(@NotNull PsiClass targetClass, @NotNull PsiMethod m,
                               @NotNull PsiParameter param, @NotNull YApiParam target) {
        PsiAnnotation annotation = PsiAnnotationUtils
                .findAnnotation(param, SpringMVCConstants.RequestHeader);
        if (Objects.nonNull(annotation)) {
            YApiHeader header = new YApiHeader();
            header.full(this.handleParamAnnotation(param, annotation));
            Set<YApiHeader> headers = target.getHeaders();
            if (Objects.isNull(headers)) {
                headers = new LinkedHashSet<>();
                target.setHeaders(headers);
            }
            headers.add(header);
        }
    }
}
