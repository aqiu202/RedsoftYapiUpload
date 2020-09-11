package com.redsoft.idea.plugin.yapiv2.req.impl;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.redsoft.idea.plugin.yapiv2.constant.SpringMVCConstants;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.model.YApiPathVariable;
import com.redsoft.idea.plugin.yapiv2.req.PsiParamFilter;
import com.redsoft.idea.plugin.yapiv2.req.abs.AbstractRequestParamResolver;
import com.redsoft.idea.plugin.yapiv2.util.DesUtils;
import com.redsoft.idea.plugin.yapiv2.util.PsiAnnotationUtils;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class RequestPathVariableResolverImpl extends AbstractRequestParamResolver {

    @NotNull
    @Override
    public PsiParamFilter getPsiParamFilter(@NotNull PsiMethod m,
            @NotNull YApiParam target) {
        return p -> PsiAnnotationUtils
                .isAnnotatedWith(p, SpringMVCConstants.PathVariable);
    }

    @Override
    public void doResolverItem(@NotNull PsiMethod m, @NotNull PsiParameter param,
            @NotNull YApiParam target) {
        PsiAnnotation annotation = PsiAnnotationUtils
                .findAnnotation(param, SpringMVCConstants.PathVariable);
        if (Objects.nonNull(annotation)) {
            YApiPathVariable pathVariable = new YApiPathVariable();
            pathVariable.full(this.handleParamAnnotation(param, annotation));
            pathVariable.setDesc(DesUtils.getParamDesc(m, param.getName()));
            Set<YApiPathVariable> pathVariables = target.getReq_params();
            if (Objects.isNull(pathVariables)) {
                pathVariables = new LinkedHashSet<>();
                target.setReq_params(pathVariables);
            }
            pathVariables.add(pathVariable);
        }
    }

}
