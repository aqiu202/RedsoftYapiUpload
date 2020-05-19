package com.redsoft.idea.plugin.yapiv2.api.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.redsoft.idea.plugin.yapiv2.api.ApiResolver;
import com.redsoft.idea.plugin.yapiv2.api.BaseInfoSetter;
import com.redsoft.idea.plugin.yapiv2.api.HttpMethodResolver;
import com.redsoft.idea.plugin.yapiv2.api.MenuSetter;
import com.redsoft.idea.plugin.yapiv2.api.PathResolver;
import com.redsoft.idea.plugin.yapiv2.api.StatusResolver;
import com.redsoft.idea.plugin.yapiv2.base.ContentTypeResolver;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.req.RequestResolver;
import com.redsoft.idea.plugin.yapiv2.req.impl.RequestContentTypeResolverImpl;
import com.redsoft.idea.plugin.yapiv2.req.impl.RequestResolverImpl;
import com.redsoft.idea.plugin.yapiv2.res.ResponseResolver;
import com.redsoft.idea.plugin.yapiv2.res.impl.ResponseContentTypeResolverImpl;
import com.redsoft.idea.plugin.yapiv2.res.impl.ResponseResolverImpl;
import com.redsoft.idea.plugin.yapiv2.xml.YApiProjectProperty;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class ApiResolverImpl implements ApiResolver {

    private final PathResolver pathResolver = new PathResolverImpl();
    private final HttpMethodResolver httpMethodResolver = new HttpMethodResolverImpl();
    private final MenuSetter menuSetter = new MenuSetterImpl();
    private final StatusResolver statusResolver = new StatusResolverImpl();
    private final ContentTypeResolver requestContentTypeResolver = new RequestContentTypeResolverImpl();
    private final ContentTypeResolver responseContentTypeResolver = new ResponseContentTypeResolverImpl();
    private final BaseInfoSetter baseInfoSetter = new BaseInfoSetterImpl();
    private final RequestResolver requestResolver;
    private final ResponseResolver responseResolver;

    public ApiResolverImpl(YApiProjectProperty property, Project project) {
        requestResolver = new RequestResolverImpl(property, project);
        responseResolver = new ResponseResolverImpl(property, project);
    }

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
