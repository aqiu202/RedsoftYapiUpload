package com.github.aqiu202.ideayapi.http.req;

import com.github.aqiu202.ideayapi.model.YApiParam;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * <b>参数逐个解析</b>
 *
 * @author aqiu 2020/7/23 4:14 下午
 **/
public interface EachRequestParamResolver extends RequestParamResolver {

    default void doResolve(@NotNull PsiClass targetClass,
                           @NotNull PsiMethod m,
                           @NotNull List<PsiParameter> parameterList,
                           @NotNull YApiParam target) {
        parameterList.forEach(p -> this.doResolverItem(targetClass, m, p, target));
    }

    void doResolverItem(@NotNull PsiClass targetClass, @NotNull PsiMethod m,
                        @NotNull PsiParameter param, @NotNull YApiParam target);
}
