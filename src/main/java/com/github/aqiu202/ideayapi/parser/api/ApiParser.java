package com.github.aqiu202.ideayapi.parser.api;

import com.github.aqiu202.ideayapi.model.YApiParam;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

/**
 * <b>接口信息解析</b>
 *
 * @author aqiu 2020/5/12 11:02 上午
 **/
public interface ApiParser {

    YApiParam parse(@NotNull PsiClass c, @NotNull PsiMethod m);
}
