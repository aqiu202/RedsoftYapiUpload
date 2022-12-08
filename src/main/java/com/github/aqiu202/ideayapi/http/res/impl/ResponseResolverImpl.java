package com.github.aqiu202.ideayapi.http.res.impl;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.http.res.ResponseResolver;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.ObjectJsonParser;
import com.github.aqiu202.ideayapi.parser.ObjectRawParser;
import com.github.aqiu202.ideayapi.parser.base.ContentTypeResolver;
import com.github.aqiu202.ideayapi.parser.impl.Json5ParserImpl;
import com.github.aqiu202.ideayapi.parser.impl.JsonSchemaParserImpl;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ResponseResolverImpl implements ResponseResolver {

    private final YApiProjectProperty property;
    private final ObjectJsonParser objectJsonParser;
    private final ObjectRawParser objectRawParser;

    public ResponseResolverImpl(YApiProjectProperty property, Project project) {
        this.property = property;
        if (property.getDataMode() == 0) {
            this.objectJsonParser = new JsonSchemaParserImpl(property, project);
        } else {
            this.objectJsonParser = new Json5ParserImpl(property, project);
        }
        this.objectRawParser = new Json5ParserImpl(property, project, false, true);
    }

    @Override
    public void resolve(@Nullable PsiType returnType, @NotNull YApiParam target) {
        if (Objects.isNull(returnType)) {
            return;
        }
        int dataMode = this.property.getDataMode();
        boolean isJsonSchema = dataMode == 0;
        target.setRes_body_is_json_schema(isJsonSchema);
        if (ContentTypeResolver.RAW_VALUE.equals(target.getRes_body_type())) {
            target.setResponse(this.objectRawParser.getRawResponse(returnType));
        } else {
            target.setResponse(this.objectJsonParser.getJson(returnType));
        }
    }
}
