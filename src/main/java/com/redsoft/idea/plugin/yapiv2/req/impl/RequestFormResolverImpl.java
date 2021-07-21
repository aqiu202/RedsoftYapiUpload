package com.redsoft.idea.plugin.yapiv2.req.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.constant.SpringMVCConstants;
import com.redsoft.idea.plugin.yapiv2.model.ValueWrapper;
import com.redsoft.idea.plugin.yapiv2.model.YApiForm;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
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

public class RequestFormResolverImpl extends AbstractCompoundRequestParamResolver {


    public RequestFormResolverImpl(Project project) {
        super(project);
    }

    @NotNull
    @Override
    public PsiParamFilter getPsiParamFilter(@NotNull PsiMethod m,
            @NotNull YApiParam target) {
        if (PsiParamUtils.noBody(target.getMethods()) || PsiParamUtils
                .hasRequestBody(m.getParameterList().getParameters())) {
            return p -> false;
        }
        //过滤掉@PathVariable注解标注的参数
        return p -> PsiAnnotationUtils.isNotAnnotatedWith(p, SpringMVCConstants.PathVariable);
    }

    @Override
    protected void doSet(@NotNull YApiParam target, Collection<ValueWrapper> wrappers) {
        Set<YApiForm> forms = wrappers.stream().map(wrapper -> {
            YApiForm form = new YApiForm();
            form.full(wrapper);
            String type = wrapper.getSource().getType().getCanonicalText();
            if (this.isFile(type)) {
                form.setType("file");
            }
            return form;
        }).collect(Collectors.toSet());
        Set<YApiForm> apiForms = target.getReq_body_form();
        if (Objects.isNull(apiForms)) {
            apiForms = new LinkedHashSet<>();
            target.setReq_body_form(apiForms);
        }
        apiForms.addAll(forms);
    }

    @Override
    protected boolean isBasicType(String typePkName) {
        return super.isBasicType(typePkName) || this.isFile(typePkName);
    }

    private boolean isFile(String typePkName) {
        return TypeUtils.isFile(typePkName);
    }

}
