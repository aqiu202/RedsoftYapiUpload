package com.github.aqiu202.ideayapi.parser.api.impl;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.http.req.RequestResolver;
import com.github.aqiu202.ideayapi.http.req.YApiParamResolver;
import com.github.aqiu202.ideayapi.http.req.impl.DocTagValueResolver;
import com.github.aqiu202.ideayapi.http.req.impl.RequestContentTypeResolverImpl;
import com.github.aqiu202.ideayapi.http.req.impl.RequestResolverImpl;
import com.github.aqiu202.ideayapi.http.req.impl.TypeDescValueResolver;
import com.github.aqiu202.ideayapi.http.res.DocTagValueHandler;
import com.github.aqiu202.ideayapi.http.res.ResponseResolver;
import com.github.aqiu202.ideayapi.http.res.impl.ResponseContentTypeResolverImpl;
import com.github.aqiu202.ideayapi.http.res.impl.ResponseResolverImpl;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.api.*;
import com.github.aqiu202.ideayapi.parser.base.ContentTypeResolver;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

public class ApiResolverImpl implements ApiResolver, DocTagValueHandler {

    private final PathResolver pathResolver = new PathResolverImpl();
    private final MenuResolver menuResolver = new MenuResolverImpl();
    private final StatusResolver statusResolver = new StatusResolverImpl();
    private final HttpMethodResolver httpMethodResolver = new HttpMethodResolverImpl();
    private final ContentTypeResolver requestContentTypeResolver = new RequestContentTypeResolverImpl();
    private final ContentTypeResolver responseContentTypeResolver = new ResponseContentTypeResolverImpl();
    private final BaseInfoResolver baseInfoResolver;
    private final RequestResolver requestResolver;
    private final ResponseResolver responseResolver;

    private final YApiParamResolver docTagValueResolver = new DocTagValueResolver();

    private final YApiParamResolver descValueResolver = new TypeDescValueResolver();

    public ApiResolverImpl(YApiProjectProperty property, Project project) {
        this.baseInfoResolver = new BaseInfoResolverImpl(property);
        this.requestResolver = new RequestResolverImpl(property, project);
        this.responseResolver = new ResponseResolverImpl(property, project);
    }

    @Override
    public void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
        this.pathResolver.resolve(c, m, target);
        this.baseInfoResolver.resolve(c, m, target);
        this.httpMethodResolver.resolve(c, m, target);
        PsiDocComment classDoc = c.getDocComment();
        PsiDocComment methodDoc = m.getDocComment();
        this.statusResolver.resolve(classDoc, methodDoc, target);
        this.menuResolver.set(c, target);
        this.requestResolver.resolve(c, m, target);
        this.requestContentTypeResolver.resolve(c, m, target);
        this.responseContentTypeResolver.resolve(c, m, target);
        this.docTagValueResolver.accept(target);
        this.descValueResolver.accept(target);
        this.responseResolver.resolve(c, m.getReturnType(), target);
    }
}
