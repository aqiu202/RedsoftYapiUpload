package com.redsoft.idea.plugin.yapiv2.base;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;

public interface ContentTypeResolver {

    String JSON = "application/json";
    String FORM = "application/x-www-form-urlencoded";
    String ROW = "text/plain";
    String JSON_VALUE = "json";
    String FORM_VALUE = "form";
    String ROW_VALUE = "row";

    void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target);
}
