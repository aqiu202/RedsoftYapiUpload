package com.redsoft.idea.plugin.yapiv2.req.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.redsoft.idea.plugin.yapiv2.constant.SpringMVCConstants;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.parser.ObjectJsonParser;
import com.redsoft.idea.plugin.yapiv2.parser.impl.Json5ParserImpl;
import com.redsoft.idea.plugin.yapiv2.req.PsiParamFilter;
import com.redsoft.idea.plugin.yapiv2.req.SimpleRequestBodyParamResolver;
import com.redsoft.idea.plugin.yapiv2.util.PsiAnnotationUtils;
import org.jetbrains.annotations.NotNull;

public class RequestBodyParamResolverImpl implements SimpleRequestBodyParamResolver {

    private final ObjectJsonParser objectJsonParser;

    public RequestBodyParamResolverImpl(Project project) {
        this.objectJsonParser = new Json5ParserImpl(project);
    }

    @NotNull
    @Override
    public PsiParamFilter getPsiParamFilter(@NotNull PsiMethod m,
            @NotNull YApiParam target) {
        return this.hasRequestBody(m.getParameterList().getParameters()) ? p -> PsiAnnotationUtils
                .isAnnotatedWith(p, SpringMVCConstants.RequestBody)
                : p -> false;
    }

    @Override
    public void doResolverItem(@NotNull PsiMethod m, @NotNull PsiParameter param,
            @NotNull YApiParam target) {
        target.setRequestBody(this.objectJsonParser.getJsonResponse(param.getType()));
    }

}
