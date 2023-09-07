package com.github.aqiu202.ideayapi.http.req.abs;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.model.EnumFields;
import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.support.YApiSupportHolder;
import com.github.aqiu202.ideayapi.util.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * <b>解析复杂类型的参数的解析抽象类(Form和Query类型，不支持多层级解析)</b>
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
    public void doResolverItem(@NotNull PsiClass targetClass, @NotNull PsiMethod m, @NotNull PsiParameter param,
                               @NotNull YApiParam target) {
        Collection<ValueWrapper> valueWrappers = this.resolvePojo(targetClass, m, param);
        if (CollectionUtils.isNotEmpty(valueWrappers)) {
            this.doSet(target, valueWrappers);
        }
    }

    protected boolean isBasicType(PsiType psiType) {
        return TypeUtils.isBasicType(psiType);
    }

    protected ValueWrapper resolveBasic(@NotNull PsiParameter param) {
        ValueWrapper valueWrapper = this.resolveParameter(param);
        valueWrapper.setExample(
                TypeUtils.getDefaultValueByPackageName(param.getType()));
        return valueWrapper;
    }

    protected ValueWrapper resolveParameter(@NotNull PsiParameter parameter) {
        ValueWrapper valueWrapper = new ValueWrapper();
        valueWrapper.setRequired(ValidUtils.getRequired(parameter));
        valueWrapper.setName(parameter.getName());
        return valueWrapper;
    }

    protected ValueWrapper resolveField(@NotNull PsiField field) {
        PsiType fType = field.getType();
        ValueWrapper valueWrapper = new ValueWrapper();
        valueWrapper.setRequired(ValidUtils.getRequired(field));
        valueWrapper.setName(field.getName());
        valueWrapper.setDesc(DesUtils.getLinkRemark(field));
        if (property.isEnableTypeDesc()) {
            valueWrapper.setTypeDesc(TypeUtils.getTypeName(fType));
        }
        valueWrapper.setExample(TypeUtils.getDefaultValueByPackageName(fType));
        return valueWrapper;
    }

    protected Collection<ValueWrapper> resolvePojo(@NotNull PsiClass targetClass,
                                                   @NotNull PsiMethod m,
                                                   @NotNull PsiParameter param) {
        PsiType paramType = param.getType();
        if (TypeUtils.isMap(paramType)) {
            return Collections.emptyList();
        }
        // 数据类型进行拆解，非json参数暂不支持多层级和数组格式
        if (paramType instanceof PsiArrayType) {
            paramType = ((PsiArrayType) paramType).getComponentType();
        }
        List<ValueWrapper> valueWrappers = new ArrayList<>();
        //如果是基本类型
        if (this.isBasicType(paramType)) {
            ValueWrapper valueWrapper;
            PsiAnnotation psiAnnotation = PsiAnnotationUtils
                    .findAnnotation(param, SpringMVCConstants.RequestParam);
            if (psiAnnotation != null) {
                valueWrapper = this.handleParamAnnotation(param, psiAnnotation);
            } else {
                valueWrapper = this.resolveBasic(param);
            }
            if (StringUtils.isBlank(valueWrapper.getDesc())) {
                String desc = DesUtils.getParamDesc(m, param.getName());
                valueWrapper.setDesc(desc);
                if (property.isEnableTypeDesc()) {
                    valueWrapper.setTypeDesc(TypeUtils.getTypeName(paramType));
                }
            }
            valueWrapper.setSource(param);
            YApiSupportHolder.supports.handleParam(valueWrapper);
            valueWrappers.add(valueWrapper);
        } else {
            PsiClass psiClass = PsiUtils.convertToClass(paramType);
            if (psiClass == null) {
                return Collections.emptyList();
            }
            if (TypeUtils.isEnum(paramType)) {
                ValueWrapper valueWrapper = this.resolveParameter(param);
                EnumFields enumFields = PsiUtils.resolveEnumFields(psiClass);
                valueWrapper.setDesc(enumFields.getDescriptionString());
                valueWrappers.add(valueWrapper);
            } else {
                for (PsiField field : psiClass.getAllFields()) {
                    if (Objects.requireNonNull(field.getModifierList())
                            .hasModifierProperty(PsiModifier.STATIC)) {
                        continue;
                    }
                    if (this.property.getIgnoredReqFieldList().contains(field.getName())) {
                        continue;
                    }
                    ValueWrapper valueWrapper = this.resolveField(field);
                    valueWrapper.setSource(field);
                    YApiSupportHolder.supports.handleField(valueWrapper);
                    valueWrappers.add(valueWrapper);
                }
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
