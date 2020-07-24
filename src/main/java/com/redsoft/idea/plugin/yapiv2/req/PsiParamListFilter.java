package com.redsoft.idea.plugin.yapiv2.req;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.constant.ServletConstants;
import com.redsoft.idea.plugin.yapiv2.util.TypeUtils;
import com.redsoft.idea.plugin.yapiv2.model.ValueWrapper;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.util.PsiUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

/**
 * <b>方法过滤请求参数</b>
 * @author aqiu
 * @date 2020/7/23 4:25 下午
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
            String name = element.getText();
            if (Strings.isEmpty(name)) {
                name = Objects.requireNonNull(psiAnnotation.findAttributeValue("value")).getText();
            }
            valueWrapper.setName(name.replace("\"", ""));
        }
        PsiAnnotationMemberValue required = psiAnnotation.findAttributeValue("required");
        if (Objects.nonNull(required)) {
            valueWrapper.setRequired(
                    required.getText().replace("\"", "")
                            .replace("false", "0")
                            .replace("true", "1"));
        }
        PsiAnnotationMemberValue defaultValue = psiAnnotation.findAttributeValue("defaultValue");
        if (Objects.nonNull(defaultValue)
                && !"\"\\n\\t\\t\\n\\t\\t\\n\\uE000\\uE001\\uE002\\n\\t\\t\\t\\t\\n\""
                .equals(defaultValue.getText())) {
            valueWrapper.setExample(defaultValue.getText().replace("\"", ""));
            valueWrapper.setRequired("0");
        }
        if (Strings.isEmpty(valueWrapper.getRequired())) {
            valueWrapper.setRequired("1");
        }
        if (Strings.isEmpty(valueWrapper.getName())) {
            valueWrapper.setName(psiParameter.getName());
        }
        if (Strings.isEmpty(valueWrapper.getExample())) {
            Object o;
            if (Objects.nonNull(o = TypeUtils
                    .getDefaultValueByPackageName((psiParameter.getType().getCanonicalText())))) {
                valueWrapper.setExample(o.toString());
            }
        }
        return valueWrapper;
    }
}
