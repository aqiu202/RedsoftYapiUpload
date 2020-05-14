package com.redsoft.idea.plugin.yapiv2.base;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;

/**
 * 路由相关信息解析（包括路由前缀添加）
 * @author aqiu
 * @date 2020/5/12 10:59 上午
 **/
public interface PathResolver {

    void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target);

}
