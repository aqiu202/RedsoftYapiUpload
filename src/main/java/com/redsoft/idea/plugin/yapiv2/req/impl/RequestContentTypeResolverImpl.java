package com.redsoft.idea.plugin.yapiv2.req.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.base.ContentTypeResolver;
import com.redsoft.idea.plugin.yapiv2.constant.HttpMethodConstants;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.util.CollectionUtils;
import com.redsoft.idea.plugin.yapiv2.util.PsiParamUtils;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class RequestContentTypeResolverImpl implements ContentTypeResolver {

    @Override
    public void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
        Set<String> methods = target.getMethods();
        //YApi默认Delete方法有body，但是设置contentType为form会报错，这里为Delete方法保持默认的json的contentType
        if (PsiParamUtils.noBody(methods) || methods.contains(HttpMethodConstants.DELETE)) {
            return;
        }
        //有body但是没有@RequestBody注解并且form参数为空，设置为form
        if (!PsiParamUtils.hasRequestBody(m.getParameterList().getParameters()) && CollectionUtils
                .isNotEmpty(target.getReq_body_form())) {
            target.setReq_body_type(FORM_VALUE);
        }
    }

}
