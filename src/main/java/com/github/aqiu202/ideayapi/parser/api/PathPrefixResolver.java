package com.github.aqiu202.ideayapi.parser.api;

import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.Nullable;

/**
 * <b>接口状态解析</b>
 *
 * @author aqiu 2020/7/23 3:47 下午
 **/
public interface PathPrefixResolver {

    String resolve(@Nullable PsiDocComment c, @Nullable PsiDocComment m);

}
