package com.github.aqiu202.ideayapi.http.req.impl;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.http.filter.PsiParamFilter;
import com.github.aqiu202.ideayapi.http.req.abs.AbstractRequestParamResolver;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.ObjectJsonParser;
import com.github.aqiu202.ideayapi.parser.impl.Json5ParserImpl;
import com.github.aqiu202.ideayapi.parser.impl.JsonSchemaParserImpl;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import org.jetbrains.annotations.NotNull;

public class RequestBodyResolverImpl extends AbstractRequestParamResolver {

    private final ObjectJsonParser objectJsonParser;

    private final int dataMode;

    public RequestBodyResolverImpl(YApiProjectProperty property, Project project) {
        this.dataMode = property.getDataMode();
        if (this.dataMode == 0) {
            this.objectJsonParser = new JsonSchemaParserImpl(property, project);
        } else {
            this.objectJsonParser = new Json5ParserImpl(property, project);
        }
    }

    @NotNull
    @Override
    public PsiParamFilter getPsiParamFilter(@NotNull PsiMethod m,
                                            @NotNull YApiParam target) {
        return p -> PsiAnnotationUtils.isAnnotatedWith(p, SpringMVCConstants.RequestBody);
    }

    @Override
    public void doResolverItem(@NotNull PsiClass targetClass, @NotNull PsiMethod m,
                               @NotNull PsiParameter param, @NotNull YApiParam target) {
        PsiType paramType = TypeUtils.resolveGenericType(targetClass, param.getType());
        target.setRequestBody(this.objectJsonParser.getJson(targetClass, paramType));
        target.setReq_body_is_json_schema(this.dataMode == 0);
    }

}
