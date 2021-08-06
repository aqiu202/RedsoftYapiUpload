package com.redsoft.idea.plugin.yapiv2.res;

import com.intellij.psi.PsiType;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <b>接口响应数据解析</b>
 * @author aqiu 2020/7/23 3:58 下午
 **/
public interface ResponseResolver {

    void resolve(@Nullable PsiType returnType, @NotNull YApiParam target);
}
