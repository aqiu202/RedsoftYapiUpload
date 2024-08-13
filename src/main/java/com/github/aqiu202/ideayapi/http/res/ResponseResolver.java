package com.github.aqiu202.ideayapi.http.res;

import com.github.aqiu202.ideayapi.model.YApiParam;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <b>接口响应数据解析</b>
 *
 * @author aqiu 2020/7/23 3:58 下午
 **/
public interface ResponseResolver {

    void resolve(PsiClass rootClass, @Nullable PsiType returnType, @NotNull YApiParam target);
}
