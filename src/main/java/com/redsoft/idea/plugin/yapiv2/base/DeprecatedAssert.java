package com.redsoft.idea.plugin.yapiv2.base;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

/**
 * <b>接口是否过时断言</b>
 * @author aqiu 2020/7/23 3:51 下午
 **/
public interface DeprecatedAssert {

    boolean isDeprecated(@NotNull PsiClass c);

    boolean isDeprecated(@NotNull PsiMethod c);

    boolean isDeprecated(@NotNull PsiClass c, @NotNull PsiMethod m);
}
