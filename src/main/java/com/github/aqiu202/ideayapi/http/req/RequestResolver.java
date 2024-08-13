package com.github.aqiu202.ideayapi.http.req;

import com.github.aqiu202.ideayapi.model.YApiParam;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

/**
 * <b>接口所有请求参数解析</b>
 *
 * @author aqiu 2020/7/23 4:22 下午
 **/
public interface RequestResolver {

    void resolve(@NotNull PsiClass rootClass, @NotNull PsiMethod m, @NotNull YApiParam target);
}
