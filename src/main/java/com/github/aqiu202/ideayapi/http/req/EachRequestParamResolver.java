package com.github.aqiu202.ideayapi.http.req;

import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.util.PsiUtils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * <b>参数逐个解析</b>
 *
 * @author aqiu 2020/7/23 4:14 下午
 **/
public interface EachRequestParamResolver extends RequestParamResolver {

    default void doResolve(@NotNull PsiClass rootClass,
                           @NotNull PsiMethod m,
                           @NotNull List<PsiParameter> parameterList,
                           @NotNull YApiParam target) {
        parameterList.forEach(p ->
                this.doResolverItem(rootClass, m, p, this.resolveParamType(rootClass, p), target)
        );
    }

    /**
     * 解析参数类型中的泛型
     *
     * @param rootClass controller类型
     * @param param     参数类型
     * @return 泛型替换后的类型
     */
    default PsiType resolveParamType(PsiClass rootClass, PsiParameter param) {
        return PsiUtils.resolveMethodGenericType(rootClass, param.getType());
    }

    void doResolverItem(@NotNull PsiClass rootClass, @NotNull PsiMethod m,
                        @NotNull PsiParameter param, PsiType paramType, @NotNull YApiParam target);
}
