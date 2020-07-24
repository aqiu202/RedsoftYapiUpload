package com.redsoft.idea.plugin.yapiv2.req.abs;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.constant.SpringMVCConstants;
import com.redsoft.idea.plugin.yapiv2.model.ValueWrapper;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.support.YApiSupportHolder;
import com.redsoft.idea.plugin.yapiv2.util.DesUtils;
import com.redsoft.idea.plugin.yapiv2.util.PsiAnnotationUtils;
import com.redsoft.idea.plugin.yapiv2.util.PsiUtils;
import com.redsoft.idea.plugin.yapiv2.util.TypeUtils;
import com.redsoft.idea.plugin.yapiv2.util.ValidUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * <b>解析复杂类型的参数的解析抽象类</b>
 * @author aqiu
 * @date 2020/7/24 1:40 下午
**/
public abstract class AbstractCompoundRequestParamResolver extends AbstractRequestParamResolver {

    protected final Project project;

    public AbstractCompoundRequestParamResolver(Project project) {
        this.project = project;
    }

    @Override
    public void doResolverItem(@NotNull PsiMethod m, @NotNull PsiParameter param,
            @NotNull YApiParam target) {
        Collection<ValueWrapper> valueWrappers = this.resolvePojo(m, param);
        this.doSet(target, valueWrappers);
    }

    protected boolean isBasicType(String typePkName) {
        return TypeUtils.isBasicType(typePkName);
    }

    protected ValueWrapper resolveBasic(@NotNull PsiParameter param) {
        ValueWrapper valueWrapper = new ValueWrapper();
        valueWrapper.setRequired(ValidUtils.getRequired(param));
        valueWrapper.setName(param.getName());
        valueWrapper.setExample(
                TypeUtils.getDefaultValueByPackageName(param.getType().getCanonicalText())
                        .toString());
        return valueWrapper;
    }

    protected ValueWrapper resolveField(@NotNull PsiField field) {
        PsiType fType = field.getType();
        ValueWrapper valueWrapper = new ValueWrapper();
        valueWrapper.setRequired(ValidUtils.getRequired(field));
        valueWrapper.setName(field.getName());
        valueWrapper.setDesc(DesUtils.getLinkRemark(field, project));
        Object obj = TypeUtils.getDefaultValueByPackageName(fType.getCanonicalText());
        if (Objects.nonNull(obj)) {
            valueWrapper.setExample(obj.toString());
        }
        return valueWrapper;
    }

    protected Collection<ValueWrapper> resolvePojo(@NotNull PsiMethod m,
            @NotNull PsiParameter param) {
        String typePkName = param.getType().getCanonicalText();
        String typeName = param.getType().getPresentableText();
        List<ValueWrapper> valueWrappers = new ArrayList<>();
        //如果是基本类型
        if (this.isBasicType(typePkName)) {
            ValueWrapper valueWrapper;
            PsiAnnotation psiAnnotation = PsiAnnotationUtils
                    .findAnnotation(param, SpringMVCConstants.RequestParam);
            if (psiAnnotation != null) {
                valueWrapper = this.handleParamAnnotation(param, psiAnnotation);
            } else {
                valueWrapper = this.resolveBasic(param);
            }
            if (Strings.isBlank(valueWrapper.getDesc())) {
                String desc = DesUtils.getParamDesc(m, param.getName()) + "(" + typeName + ")";
                valueWrapper.setDesc(desc);
            }
            valueWrapper.setOrigin(param);
            YApiSupportHolder.supports.handleParam(param, valueWrapper);
            valueWrappers.add(valueWrapper);
        } else {
            PsiClass psiClass = PsiUtils.findPsiClass(this.project, typePkName);
            for (PsiField field : Objects.requireNonNull(psiClass).getAllFields()) {
                if (Objects.requireNonNull(field.getModifierList())
                        .hasModifierProperty(PsiModifier.STATIC)) {
                    continue;
                }
                ValueWrapper valueWrapper = this.resolveField(field);
                valueWrapper.setOrigin(field);
                YApiSupportHolder.supports.handleField(field, valueWrapper);
                valueWrappers.add(valueWrapper);
            }
        }
        return valueWrappers;
    }

    protected abstract void doSet(@NotNull YApiParam target, Collection<ValueWrapper> wrappers);
}
