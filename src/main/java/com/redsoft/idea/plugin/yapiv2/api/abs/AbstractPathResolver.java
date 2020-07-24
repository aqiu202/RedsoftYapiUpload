package com.redsoft.idea.plugin.yapiv2.api.abs;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.redsoft.idea.plugin.yapiv2.api.BasePathResolver;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * <b>根据SpringMVC注解解析路由</b>
 * @author aqiu
 * @date 2020/7/23 3:44 下午
**/
public abstract class AbstractPathResolver implements BasePathResolver {

    @Override
    public String getPathByAnnotation(@NotNull PsiAnnotation psiAnnotation) {
        PsiAnnotationMemberValue element = psiAnnotation.findAttributeValue("path");
        if (element == null) {
            return "";
        }
        String value = element.getText();
        if ("{}".equals(value)) {
            value = Objects.requireNonNull(psiAnnotation.findAttributeValue("value")).getText();
        }
        return "{}".equals(value) ? "" : value.replace("\"", "");
    }

}
