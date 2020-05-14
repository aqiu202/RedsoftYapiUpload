package com.redsoft.idea.plugin.yapiv2.base;

import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;

/**
 * <b>HttpMethod解析</b>
 * @author aqiu
 * @date 2020/5/12 11:02 上午
 **/
public interface HttpMethodResolver {

    void resolve(@NotNull PsiMethod method, @NotNull YApiParam target);

}
