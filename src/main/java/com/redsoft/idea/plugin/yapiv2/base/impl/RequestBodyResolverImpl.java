package com.redsoft.idea.plugin.yapiv2.base.impl;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.redsoft.idea.plugin.yapiv2.base.PsiParamFilter;
import com.redsoft.idea.plugin.yapiv2.base.SimpleRequestBodyParamResolver;
import com.redsoft.idea.plugin.yapiv2.config.ProjectConfigReader;
import com.redsoft.idea.plugin.yapiv2.constant.SpringMVCConstants;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.util.ProjectHolder;
import com.redsoft.idea.plugin.yapiv2.util.PsiAnnotationUtils;
import org.jetbrains.annotations.NotNull;

public class RequestBodyResolverImpl implements SimpleRequestBodyParamResolver {

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
        target.setRequestBody(new JsonSchemaParserImpl(ProjectConfigReader.read(
                ProjectHolder.getCurrentProject())).getSchemaResponse(param.getType()));
    }

}
