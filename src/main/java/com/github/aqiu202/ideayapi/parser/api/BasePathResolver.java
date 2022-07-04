package com.github.aqiu202.ideayapi.parser.api;

import com.github.aqiu202.ideayapi.model.YApiParam;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * <b>接口路由解析抽象解析器</b>
 *
 * @author aqiu 2020/7/23 3:44 下午
 **/
public interface BasePathResolver {

    void resolve(@NotNull PsiModifierListOwner psiModifierListOwner, @NotNull YApiParam target);

    Set<String> getPathByAnnotation(@NotNull PsiAnnotation psiAnnotation);

}
