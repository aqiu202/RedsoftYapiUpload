package com.redsoft.idea.plugin.yapiv2.res;

import com.intellij.psi.PsiType;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ResponseResolver {

    void resolve(@Nullable PsiType returnType, @NotNull YApiParam target);
}
