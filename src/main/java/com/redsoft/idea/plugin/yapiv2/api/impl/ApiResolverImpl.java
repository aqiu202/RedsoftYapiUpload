package com.redsoft.idea.plugin.yapiv2.api.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.redsoft.idea.plugin.yapiv2.api.ApiResolver;
import com.redsoft.idea.plugin.yapiv2.api.BaseInfoResolver;
import com.redsoft.idea.plugin.yapiv2.api.HttpMethodResolver;
import com.redsoft.idea.plugin.yapiv2.api.MenuResolver;
import com.redsoft.idea.plugin.yapiv2.api.PathResolver;
import com.redsoft.idea.plugin.yapiv2.api.StatusResolver;
import com.redsoft.idea.plugin.yapiv2.base.ContentTypeResolver;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.model.YApiStatus;
import com.redsoft.idea.plugin.yapiv2.req.RequestResolver;
import com.redsoft.idea.plugin.yapiv2.req.impl.RequestContentTypeResolverImpl;
import com.redsoft.idea.plugin.yapiv2.req.impl.RequestResolverImpl;
import com.redsoft.idea.plugin.yapiv2.res.DocTagValueHandler;
import com.redsoft.idea.plugin.yapiv2.res.ResponseResolver;
import com.redsoft.idea.plugin.yapiv2.res.impl.ResponseContentTypeResolverImpl;
import com.redsoft.idea.plugin.yapiv2.res.impl.ResponseResolverImpl;
import com.redsoft.idea.plugin.yapiv2.util.CollectionUtils;
import com.redsoft.idea.plugin.yapiv2.xml.YApiProjectProperty;
import java.util.Objects;
import java.util.function.Consumer;
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

    private final Consumer<YApiParam> docTagValueResolver = (param) -> {
        param.setTitle(this.handleDocTagValue(param.getTitle()));
        param.setMenu(this.handleDocTagValue(param.getMenu()));
        param.setMenuDesc(this.handleDocTagValue(param.getMenuDesc()));
        param.setStatus(YApiStatus.getStatus(this.handleDocTagValue(param.getStatus())));
        if (CollectionUtils.isNotEmpty(param.getParams())) {
            param.getParams()
                    .forEach(query -> query.setDesc(this.handleDocTagValue(query.getDesc())));
        }
        if (CollectionUtils.isNotEmpty(param.getReq_body_form())) {
            param.getReq_body_form()
                    .forEach(form -> form.setDesc(this.handleDocTagValue(form.getDesc())));
        }
        if (CollectionUtils.isNotEmpty(param.getReq_params())) {
            param.getReq_params().forEach(pathVariable -> pathVariable
                    .setDesc(this.handleDocTagValue(pathVariable.getDesc())));
        }
    };

    public ApiResolverImpl(YApiProjectProperty property, Project project) {
        requestResolver = new RequestResolverImpl(property, project);
        responseResolver = new ResponseResolverImpl(property, project);
    }

    @Override
    public void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
        pathResolver.resolve(c, m, target);
        baseInfoResolver.resolve(c, m, target);
        httpMethodResolver.resolve(m, target);
        requestContentTypeResolver.resolve(c, m, target);
        responseContentTypeResolver.resolve(c, m, target);
        PsiDocComment classDoc = c.getDocComment();
        PsiDocComment methodDoc = m.getDocComment();
        statusResolver.resolve(classDoc, methodDoc, target);
        if (Objects.nonNull(classDoc)) {
            menuResolver.set(classDoc, target);
        }
        requestResolver.resolve(m, target);
        docTagValueResolver.accept(target);
        responseResolver.resolve(m.getReturnType(), target);
    }
}
