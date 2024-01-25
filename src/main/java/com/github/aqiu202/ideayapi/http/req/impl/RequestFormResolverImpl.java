package com.github.aqiu202.ideayapi.http.req.impl;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.http.filter.PsiParamFilter;
import com.github.aqiu202.ideayapi.http.req.abs.AbstractCompoundRequestParamResolver;
import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.model.YApiForm;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.type.PsiDescriptor;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.github.aqiu202.ideayapi.util.PsiParamUtils;
import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * form参数处理器
 *
 * @author aqiu
 */
public class RequestFormResolverImpl extends AbstractCompoundRequestParamResolver {

    public RequestFormResolverImpl(YApiProjectProperty property, Project project) {
        super(property, project);
    }

    @NotNull
    @Override
    public PsiParamFilter getPsiParamFilter(@NotNull PsiMethod m,
                                            @NotNull YApiParam target) {
        if (PsiParamUtils.noBody(target.getMethod()) || PsiParamUtils
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
            PsiDescriptor source = wrapper.getSource();
            PsiType type = source.getType();
            if (type != null && this.isFile(type)) {
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
    protected boolean isBasicType(PsiType psiType) {
        return super.isBasicType(psiType) || this.isFile(psiType);
    }

    private boolean isFile(PsiType psiType) {
        return TypeUtils.isFile(psiType);
    }

}
