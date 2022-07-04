package com.github.aqiu202.ideayapi.parser.api;

import com.github.aqiu202.ideayapi.model.YApiParam;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <b>接口状态解析</b>
 *
 * @author aqiu 2020/5/12 11:22 上午
 **/
public interface StatusResolver {

    void resolve(@Nullable PsiDocComment classDoc, @Nullable PsiDocComment methodDoc,
                 @NotNull YApiParam target);
}
