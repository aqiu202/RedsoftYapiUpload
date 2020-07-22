package com.redsoft.idea.plugin.yapiv2.res.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiType;
import com.redsoft.idea.plugin.yapiv2.base.ContentTypeResolver;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.parser.ObjectJsonParser;
import com.redsoft.idea.plugin.yapiv2.parser.ObjectRawParser;
import com.redsoft.idea.plugin.yapiv2.parser.impl.Json5ParserImpl;
import com.redsoft.idea.plugin.yapiv2.parser.impl.JsonSchemaParserImpl;
import com.redsoft.idea.plugin.yapiv2.res.ResponseResolver;
import com.redsoft.idea.plugin.yapiv2.xml.YApiProjectProperty;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        this.objectRawParser = new Json5ParserImpl(project, false);
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
            target.setResponse(this.objectJsonParser.getJsonResponse(returnType));
        }
    }
}
