package com.redsoft.idea.plugin.yapiv2.api;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * <b>接口路由解析抽象解析器</b>
 * @author aqiu 2020/7/23 3:44 下午
 **/
public interface BasePathResolver {

    void resolve(@NotNull PsiModifierListOwner psiModifierListOwner, @NotNull YApiParam target);

    Set<String> getPathByAnnotation(@NotNull PsiAnnotation psiAnnotation);

}
