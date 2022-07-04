package com.github.aqiu202.ideayapi.http.req.abs;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.support.YApiSupportHolder;
import com.github.aqiu202.ideayapi.util.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.jgoodies.common.base.Strings;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <b>解析复杂类型的参数的解析抽象类</b>
 *
 * @author aqiu 2020/7/24 1:40 下午
 **/
public abstract class AbstractCompoundRequestParamResolver extends AbstractRequestParamResolver {

    protected final Project project;
    protected final YApiProjectProperty property;

    public AbstractCompoundRequestParamResolver(YApiProjectProperty property, Project project) {
        this.property = property;
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
        if (property.isEnableTypeDesc()) {
            valueWrapper.setTypeDesc(fType.getPresentableText());
        }
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
        if (typePkName.contains("[]")) {
            typePkName = typePkName.replace("[]", "");
        }
        int index;
        while ((index = typePkName.indexOf("<")) != -1) {
            typePkName = typePkName.substring(index + 1);
        }
        if (typePkName.contains(">")) {
            typePkName = typePkName.replace(">", "");
        }
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
                String desc = DesUtils.getParamDesc(m, param.getName());
                valueWrapper.setDesc(desc);
                if (property.isEnableTypeDesc()) {
                    valueWrapper.setTypeDesc(typeName);
                }
            }
            valueWrapper.setSource(param);
            YApiSupportHolder.supports.handleParam(valueWrapper);
            valueWrappers.add(valueWrapper);
        } else {
            PsiClass psiClass = PsiUtils.findPsiClass(this.project, typePkName);
            for (PsiField field : Objects.requireNonNull(psiClass).getAllFields()) {
                if (Objects.requireNonNull(field.getModifierList())
                        .hasModifierProperty(PsiModifier.STATIC)) {
                    continue;
                }
                ValueWrapper valueWrapper = this.resolveField(field);
                valueWrapper.setSource(field);
                YApiSupportHolder.supports.handleField(valueWrapper);
                valueWrappers.add(valueWrapper);
            }
        }
        return valueWrappers;
    }

    /**
     * @param target   参数
     * @param wrappers 参数值
     */
    protected abstract void doSet(@NotNull YApiParam target, Collection<ValueWrapper> wrappers);
}
