package com.github.aqiu202.ideayapi.parser.base;

import com.github.aqiu202.ideayapi.parser.base.impl.DeprecatedAssertImpl;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierListOwner;
import org.jetbrains.annotations.NotNull;

/**
 * <b>接口是否过时断言</b>
 *
 * @author aqiu 2020/7/23 3:51 下午
 **/
public interface DeprecatedAssert {

    DeprecatedAssert instance = new DeprecatedAssertImpl();

    boolean isDeprecated(@NotNull PsiModifierListOwner c);

    boolean isDeprecated(@NotNull PsiField c);

    boolean isDeprecated(@NotNull PsiClass c);

    boolean isDeprecated(@NotNull PsiMethod c);

    boolean isDeprecated(@NotNull PsiClass c, @NotNull PsiMethod m);
}
