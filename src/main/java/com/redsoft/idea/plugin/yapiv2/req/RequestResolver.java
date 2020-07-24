package com.redsoft.idea.plugin.yapiv2.req;

import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;

/**
 * <b>接口所有请求参数解析</b>
 * @author aqiu
 * @date 2020/7/23 4:22 下午
**/
public interface RequestResolver {

    void resolve(@NotNull PsiMethod m, @NotNull YApiParam target);
}
