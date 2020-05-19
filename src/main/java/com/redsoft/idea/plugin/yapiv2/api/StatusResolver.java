package com.redsoft.idea.plugin.yapiv2.api;

import com.intellij.psi.javadoc.PsiDocComment;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <b>接口状态解析</b>
 * @author aqiu
 * @date 2020/5/12 11:22 上午
 **/
public interface StatusResolver {

    void resolve(@Nullable PsiDocComment classDoc, @Nullable PsiDocComment methodDoc,
            @NotNull YApiParam target);
}
