package com.redsoft.idea.plugin.yapiv2.base;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;

public interface BasePathResolver {

    void resolve(@NotNull PsiModifierListOwner psiModifierListOwner, @NotNull YApiParam target);

    String getPathByAnnotation(@NotNull PsiAnnotation psiAnnotation);

}
