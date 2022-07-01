package com.redsoft.idea.plugin.yapiv2.api.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.redsoft.idea.plugin.yapiv2.api.*;
import com.redsoft.idea.plugin.yapiv2.base.ContentTypeResolver;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.req.RequestResolver;
import com.redsoft.idea.plugin.yapiv2.req.YApiParamResolver;
import com.redsoft.idea.plugin.yapiv2.req.impl.DocTagValueResolver;
import com.redsoft.idea.plugin.yapiv2.req.impl.RequestContentTypeResolverImpl;
import com.redsoft.idea.plugin.yapiv2.req.impl.RequestResolverImpl;
import com.redsoft.idea.plugin.yapiv2.req.impl.TypeDescValueResolver;
import com.redsoft.idea.plugin.yapiv2.res.DocTagValueHandler;
import com.redsoft.idea.plugin.yapiv2.res.ResponseResolver;
import com.redsoft.idea.plugin.yapiv2.res.impl.ResponseContentTypeResolverImpl;
import com.redsoft.idea.plugin.yapiv2.res.impl.ResponseResolverImpl;
import com.redsoft.idea.plugin.yapiv2.xml.YApiProjectProperty;
import org.jetbrains.annotations.NotNull;

public class ApiResolverImpl implements ApiResolver, DocTagValueHandler {

    private final PathResolver pathResolver = new PathResolverImpl();
    private final HttpMethodResolver httpMethodResolver = new HttpMethodResolverImpl();
    private final MenuResolver menuResolver = new MenuResolverImpl();
    private final StatusResolver statusResolver = new StatusResolverImpl();
    private final ContentTypeResolver requestContentTypeResolver = new RequestContentTypeResolverImpl();
    private final ContentTypeResolver responseContentTypeResolver = new ResponseContentTypeResolverImpl();
    private final BaseInfoResolver baseInfoResolver = new BaseInfoResolverImpl();
    private final RequestResolver requestResolver;
    private final ResponseResolver responseResolver;

    private final YApiParamResolver docTagValueResolver = new DocTagValueResolver();

    private final YApiParamResolver descValueResolver = new TypeDescValueResolver();

    public ApiResolverImpl(YApiProjectProperty property, Project project) {
        requestResolver = new RequestResolverImpl(property, project);
        responseResolver = new ResponseResolverImpl(property, project);
    }

    @Override
    public void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
        pathResolver.resolve(c, m, target);
        baseInfoResolver.resolve(c, m, target);
        httpMethodResolver.resolve(m, target);
        PsiDocComment classDoc = c.getDocComment();
        PsiDocComment methodDoc = m.getDocComment();
        statusResolver.resolve(classDoc, methodDoc, target);
        menuResolver.set(c, target);
        requestResolver.resolve(m, target);
        requestContentTypeResolver.resolve(c, m, target);
        responseContentTypeResolver.resolve(c, m, target);
        docTagValueResolver.accept(target);
        descValueResolver.accept(target);
        responseResolver.resolve(m.getReturnType(), target);
    }
}
