package com.redsoft.idea.plugin.yapiv2.base;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;

/**
 * <b>接口信息解析</b>
 * @author aqiu
 * @date 2020/5/12 11:02 上午
 **/
public interface ApiResolver {

    void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target);

}
