package com.redsoft.idea.plugin.yapiv2.base.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.redsoft.idea.plugin.yapiv2.base.ApiResolver;
import com.redsoft.idea.plugin.yapiv2.base.BaseInfoSetter;
import com.redsoft.idea.plugin.yapiv2.base.ContentTypeResolver;
import com.redsoft.idea.plugin.yapiv2.base.HttpMethodResolver;
import com.redsoft.idea.plugin.yapiv2.base.MenuSetter;
import com.redsoft.idea.plugin.yapiv2.base.PathResolver;
import com.redsoft.idea.plugin.yapiv2.base.RequestResolver;
import com.redsoft.idea.plugin.yapiv2.base.ResponseResolver;
import com.redsoft.idea.plugin.yapiv2.base.StatusResolver;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class ApiResolverImpl implements ApiResolver {


    private final PathResolver pathResolver = new PathResolverImpl();
    private final HttpMethodResolver httpMethodResolver = new HttpMethodResolverImpl();
    private final MenuSetter menuSetter = new MenuSetterImpl();
    private final StatusResolver statusResolver = new StatusResolverImpl();
    private final ContentTypeResolver requestContentTypeResolver = new RequestContentTypeResolverImpl();
    private final ContentTypeResolver responseContentTypeResolver = new ResponseContentTypeResolverImpl();
    private final RequestResolver requestResolver = new RequestResolverImpl();
    private final BaseInfoSetter baseInfoSetter = new BaseInfoSetterImpl();
    private final ResponseResolver responseResolver = new ResponseResolverImpl();

    @Override
    public void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
        pathResolver.resolve(c, m, target);
        baseInfoSetter.set(c, m, target);
        httpMethodResolver.resolve(m, target);
        requestContentTypeResolver.resolve(c, m, target);
        responseContentTypeResolver.resolve(c, m, target);
        PsiDocComment classDoc = c.getDocComment();
        PsiDocComment methodDoc = m.getDocComment();
        statusResolver.resolve(classDoc, methodDoc, target);
        if (Objects.nonNull(classDoc)) {
            menuSetter.set(classDoc, target);
        }
        requestResolver.resolve(m, target);
        responseResolver.resolve(m.getReturnType(), target);
    }
}
