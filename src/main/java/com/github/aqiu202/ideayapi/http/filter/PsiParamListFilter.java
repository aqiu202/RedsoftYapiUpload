package com.github.aqiu202.ideayapi.http.filter;

import com.github.aqiu202.ideayapi.constant.ServletConstants;
import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.github.aqiu202.ideayapi.util.StringUtils;
import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <b>方法过滤请求参数</b>
 *
 * @author aqiu 2020/7/23 4:25 下午
 **/
public interface PsiParamListFilter {

    default List<PsiParameter> filter(@NotNull PsiMethod m, @NotNull YApiParam target) {
        return Stream.of(m.getParameterList().getParameters())
                .filter(p ->
                        !this.isInnerParameterType(p) &&
                                !TypeUtils.isMap(p.getType()) &&
                                this.getPsiParamFilter(m, target).test(p))
                .collect(Collectors.toList());
    }

    default boolean isInnerParameterType(PsiParameter p) {
        String typePkName = TypeUtils.getTypePkName(p.getType());
        return StringUtils.equalsAny(typePkName, ServletConstants.HttpServletTypes);
    }

    @NotNull
    PsiParamFilter getPsiParamFilter(@NotNull PsiMethod m, @NotNull YApiParam target);

    default ValueWrapper handleParamAnnotation(@NotNull PsiParameter psiParameter,
                                               @NotNull PsiAnnotation psiAnnotation) {
        ValueWrapper valueWrapper = new ValueWrapper();
        String nameVal = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation, "name");
        if (StringUtils.isBlank(nameVal)) {
            nameVal = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation);
        }
        valueWrapper.setName(nameVal);
        String requiredVal = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation, "required");
        valueWrapper.setRequired(Objects.equals("false", requiredVal) ? "0" : "1");
        String defaultValue = PsiAnnotationUtils.getPsiAnnotationAttributeValue(psiAnnotation, "defaultValue");
        if (!Objects.equals("\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n", defaultValue)) {
            valueWrapper.setExample(defaultValue);
            valueWrapper.setRequired("0");
        }
        if (StringUtils.isBlank(valueWrapper.getName())) {
            valueWrapper.setName(psiParameter.getName());
        }
        if (StringUtils.isBlank(valueWrapper.getExample())) {
            valueWrapper.setExample(TypeUtils
                    .getDefaultValueByPackageName((psiParameter.getType())));
        }
        return valueWrapper;
    }
}
