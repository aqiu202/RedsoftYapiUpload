package com.redsoft.idea.plugin.yapiv2.req.impl;

import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.req.RequestParamResolver;
import com.redsoft.idea.plugin.yapiv2.req.RequestResolver;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class RequestResolverImpl implements RequestResolver {

    private final RequestParamResolver headerResolver = new RequestHeaderResolverImpl();
    private final RequestParamResolver pathVariableResolver = new RequestPathVariableResolverImpl();
    private final RequestParamResolver formResolver = new RequestFormResolverImpl();
    private final RequestParamResolver queryResolver = new RequestQueryResolverImpl();
    private final RequestParamResolver bodyResolver = new RequestBodyResolverImpl();

    private final List<RequestParamResolver> paramResolvers = Arrays
            .asList(pathVariableResolver, formResolver, queryResolver,
                    bodyResolver, headerResolver);

    @Override
    public void resolve(@NotNull PsiMethod m, @NotNull YApiParam target) {
        this.paramResolvers.forEach(r -> r.resolve(m, target));
    }

}
