package com.redsoft.idea.plugin.yapiv2.base.impl;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.redsoft.idea.plugin.yapiv2.base.PsiParamFilter;
import com.redsoft.idea.plugin.yapiv2.base.SimpleRequestParamResolver;
import com.redsoft.idea.plugin.yapiv2.constant.SpringMVCConstants;
import com.redsoft.idea.plugin.yapiv2.model.YApiHeader;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.util.PsiAnnotationUtils;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class RequestHeaderResolverImpl implements SimpleRequestParamResolver {

    private final PsiParamFilter psiParamFilter = (param) ->
            PsiAnnotationUtils.isAnnotatedWith(param, SpringMVCConstants.RequestHeader);

    @NotNull
    @Override
    public PsiParamFilter getPsiParamFilter(@NotNull PsiMethod m,
            @NotNull YApiParam target) {
        return this.psiParamFilter;
    }

    @Override
    public void doResolverItem(@NotNull PsiMethod m, @NotNull PsiParameter param,
            @NotNull YApiParam target) {
        PsiAnnotation annotation = param.getAnnotation(SpringMVCConstants.RequestHeader);
        if (Objects.nonNull(annotation)) {
            YApiHeader header = new YApiHeader();
            header.full(this.handleParamAnnotation(param, annotation));
            Set<YApiHeader> headers = target.getHeaders();
            if (Objects.isNull(headers)) {
                headers = new HashSet<>();
                target.setHeaders(headers);
            }
            headers.add(header);
        }
    }
}
