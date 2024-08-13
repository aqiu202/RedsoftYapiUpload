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
import com.github.aqiu202.ideayapi.util.StringUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class ApiResolverImpl implements ApiParser, DocTagValueHandler {

    private final YApiProjectProperty property;
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
        this.property = property;
        this.baseInfoResolver = new BaseInfoResolverImpl(property);
        this.requestResolver = new RequestResolverImpl(property, project);
        this.responseResolver = new ResponseResolverImpl(property, project);
    }

    @Override
    public List<YApiParam> parse(@NotNull PsiClass c, @NotNull PsiMethod m) {
        YApiParam target = new YApiParam();
        this.responseContentTypeResolver.resolve(c, m, target);
        // 根据配置忽略非Restful接口
        if (this.property.isIgnoreViewUrl()
                && StringUtils.equals(ContentTypeResolver.RAW_VALUE, target.getRes_body_type())) {
            return null;
        }
        this.pathResolver.resolve(c, m, target);
        this.baseInfoResolver.resolve(c, m, target);
        this.statusResolver.resolve(c.getDocComment(), m.getDocComment(), target);
        this.menuResolver.set(c, target);
        List<String> methods = this.httpMethodResolver.resolve(c, m, target);
        return methods.stream().map(method -> new YApiParam(method, target))
                .peek(p -> {
                    this.requestResolver.resolve(c, m, p);
                    this.requestContentTypeResolver.resolve(c, m, p);
                    this.docTagValueResolver.accept(p);
                    this.descValueResolver.accept(p);
                    this.responseResolver.resolve(c, m.getReturnType(), p);
                }).collect(Collectors.toList());
    }
}
