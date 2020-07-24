package com.redsoft.idea.plugin.yapiv2.req.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.redsoft.idea.plugin.yapiv2.constant.SpringMVCConstants;
import com.redsoft.idea.plugin.yapiv2.model.ValueWrapper;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.model.YApiQuery;
import com.redsoft.idea.plugin.yapiv2.req.PsiParamFilter;
import com.redsoft.idea.plugin.yapiv2.req.abs.AbstractCompoundRequestParamResolver;
import com.redsoft.idea.plugin.yapiv2.util.PsiAnnotationUtils;
import com.redsoft.idea.plugin.yapiv2.util.PsiParamUtils;
import com.redsoft.idea.plugin.yapiv2.util.TypeUtils;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class RequestQueryResolverImpl extends AbstractCompoundRequestParamResolver {

    public RequestQueryResolverImpl(Project project) {
        super(project);
    }

    @NotNull
    @Override
    public PsiParamFilter getPsiParamFilter(@NotNull PsiMethod m,
            @NotNull YApiParam target) {
        //没有body的方法，除去文件类型的参数和被@PathVariable注解标注的参数
        if (PsiParamUtils.noBody(target.getMethod())) {
            return p -> !TypeUtils.isFile(p.getType().getCanonicalText()) && PsiAnnotationUtils
                    .isNotAnnotatedWith(p, SpringMVCConstants.PathVariable);
        }
        //有body的方法，含有@RequestBody注解的参数以外的其他参数，也除去文件类型的参数和被@PathVariable注解标注的参数
        PsiParameter[] parameters = m.getParameterList().getParameters();
        return PsiParamUtils.hasRequestBody(parameters) ? (p -> PsiAnnotationUtils
                .isNotAnnotatedWith(p, SpringMVCConstants.RequestBody) && !TypeUtils
                .isFile(p.getType().getCanonicalText()) && PsiAnnotationUtils
                .isNotAnnotatedWith(p, SpringMVCConstants.PathVariable))
                //有body的方法且参数中没有@RequestBody注解，当做Form而不是Query参数
                : (p -> false);
    }

    @Override
    protected void doSet(@NotNull YApiParam target, Collection<ValueWrapper> wrappers) {
        Set<YApiQuery> queries = wrappers.stream().map(wrapper -> {
            YApiQuery query = new YApiQuery();
            query.full(wrapper);
            return query;
        }).collect(Collectors.toSet());
        Set<YApiQuery> apiQueries = target.getParams();
        if (Objects.isNull(apiQueries)) {
            apiQueries = new LinkedHashSet<>();
            target.setParams(apiQueries);
        }
        apiQueries.addAll(queries);
    }
}
