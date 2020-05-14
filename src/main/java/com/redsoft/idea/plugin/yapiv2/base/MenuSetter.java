package com.redsoft.idea.plugin.yapiv2.base;

import com.intellij.psi.javadoc.PsiDocComment;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;

public interface MenuSetter {

    void set(@NotNull PsiDocComment docComment, @NotNull YApiParam target);

}
