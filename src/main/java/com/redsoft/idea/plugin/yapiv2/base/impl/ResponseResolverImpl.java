package com.redsoft.idea.plugin.yapiv2.base.impl;

import com.intellij.psi.PsiType;
import com.redsoft.idea.plugin.yapiv2.base.ResponseResolver;
import com.redsoft.idea.plugin.yapiv2.config.ProjectConfigReader;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.util.ProjectHolder;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResponseResolverImpl implements ResponseResolver {

    @Override
    public void resolve(@Nullable PsiType returnType, @NotNull YApiParam target) {
        if (Objects.isNull(returnType)) {
            return;
        }
        JsonSchemaParserImpl jsonSchemaParser = new JsonSchemaParserImpl(ProjectConfigReader.read(
                ProjectHolder.getCurrentProject()));
        if ("raw".equals(target.getRes_body_type())) {
            target.setResponse(jsonSchemaParser.getRawResponse(returnType));
        } else {
            target.setResponse(jsonSchemaParser.getSchemaResponse(returnType));
        }
    }
}
