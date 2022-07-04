package com.github.aqiu202.ideayapi.http.filter;

import com.github.aqiu202.ideayapi.constant.ServletConstants;
import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.github.aqiu202.ideayapi.util.TypeUtils;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.jgoodies.common.base.Strings;
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
                        this.getPsiParamFilter(m, target).test(p) &&
                                !TypeUtils.isMap(p.getType()) &&
                                !(ServletConstants.HttpServletRequest
                                        .equals(p.getType().getCanonicalText())
                                        || ServletConstants.HttpServletResponse
                                        .equals(p.getType().getCanonicalText())
                                        || ServletConstants.HttpSession
                                        .equals(p.getType().getCanonicalText()))
                )
                .collect(Collectors.toList());
    }

    @NotNull
    PsiParamFilter getPsiParamFilter(@NotNull PsiMethod m, @NotNull YApiParam target);

    default ValueWrapper handleParamAnnotation(@NotNull PsiParameter psiParameter,
                                               @NotNull PsiAnnotation psiAnnotation) {
        ValueWrapper valueWrapper = new ValueWrapper();
        PsiAnnotationMemberValue element = psiAnnotation.findAttributeValue("name");
        if (Objects.nonNull(element)) {
            String name = PsiAnnotationUtils.resolveAnnotationValue(element.getText());
            if (Strings.isBlank(name)) {
                name = PsiAnnotationUtils.resolveAnnotationValue(Objects.requireNonNull(psiAnnotation.findAttributeValue("value")).getText());
            }
            valueWrapper.setName(name);
        }
        PsiAnnotationMemberValue required = psiAnnotation.findAttributeValue("required");
        if (Objects.nonNull(required)) {
            valueWrapper.setRequired(
                    PsiAnnotationUtils.resolveAnnotationValue(required.getText())
                            .replace("false", "0")
                            .replace("true", "1"));
        }
        PsiAnnotationMemberValue defaultValue = psiAnnotation.findAttributeValue("defaultValue");
        String stringValue;
        if (Objects.nonNull(defaultValue)
                && !"\\n\\t\\t\\n\\t\\t\\n\\uE000\\uE001\\uE002\\n\\t\\t\\t\\t\\n"
                .equals((stringValue = PsiAnnotationUtils.resolveAnnotationValue(defaultValue.getText())))) {
            valueWrapper.setExample(stringValue);
            valueWrapper.setRequired("0");
        }
        if (Strings.isBlank(valueWrapper.getRequired())) {
            valueWrapper.setRequired("1");
        }
        if (Strings.isBlank(valueWrapper.getName())) {
            valueWrapper.setName(psiParameter.getName());
        }
        if (Strings.isBlank(valueWrapper.getExample())) {
            Object o;
            if (Objects.nonNull(o = TypeUtils
                    .getDefaultValueByPackageName((psiParameter.getType().getCanonicalText())))) {
                valueWrapper.setExample(o.toString());
            }
        }
        return valueWrapper;
    }
}
