package com.github.aqiu202.ideayapi.http.req.impl;

import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.base.ContentTypeResolver;
import com.github.aqiu202.ideayapi.util.CollectionUtils;
import com.github.aqiu202.ideayapi.util.PsiParamUtils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class RequestContentTypeResolverImpl implements ContentTypeResolver {

    @Override
    public void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
        Set<String> methods = target.getMethods();
        //YApi默认Delete方法有body，但是设置contentType为form会报错，这里为Delete方法保持默认的json的contentType
        if (PsiParamUtils.noBody(methods)) {
            return;
        }
        //有body但是没有@RequestBody注解并且form参数为空，设置为form
        if (!PsiParamUtils.hasRequestBody(m.getParameterList().getParameters()) && CollectionUtils
                .isNotEmpty(target.getReq_body_form())) {
            target.setReq_body_type(FORM_VALUE);
        }
    }

}
