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
import com.redsoft.idea.plugin.yapiv2.res.DocTagValueHandler;
import java.util.Objects;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public class HttpClientFileResolverImpl implements ApiResolver, DocTagValueHandler {

    private final PathResolver pathResolver = new PathResolverImpl();
    private final HttpMethodResolver httpMethodResolver = new HttpMethodResolverImpl();
    private final MenuSetter menuSetter = new MenuSetterImpl();
    private final StatusResolver statusResolver = new StatusResolverImpl();
    private final ContentTypeResolver requestContentTypeResolver = new RequestContentTypeResolverImpl();
    private final BaseInfoSetter baseInfoSetter = new BaseInfoSetterImpl();
    private final RequestResolver requestResolver;

    private final Consumer<YApiParam> docTagValueResolver = (param) -> {
        param.setTitle(this.handleDocTagValue(param.getTitle()));
        param.setMenu(this.handleDocTagValue(param.getMenu()));
    };

    public HttpClientFileResolverImpl(Project project) {
        requestResolver = new RequestResolverImpl(project);
    }

    @Override
    public void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
        pathResolver.resolve(c, m, target);
        baseInfoSetter.set(c, m, target);
        httpMethodResolver.resolve(m, target);
        requestContentTypeResolver.resolve(c, m, target);
        PsiDocComment classDoc = c.getDocComment();
        PsiDocComment methodDoc = m.getDocComment();
        statusResolver.resolve(classDoc, methodDoc, target);
        if (Objects.nonNull(classDoc)) {
            menuSetter.set(classDoc, target);
        }
        requestResolver.resolve(m, target);
        docTagValueResolver.accept(target);
    }
}
