package com.redsoft.idea.plugin.yapiv2.base;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;

public interface ContentTypeResolver {

    final static String JSON = "application/json";
    final static String FORM = "application/x-www-form-urlencoded";
    final static String ROW = "text/plain";
    final static String JSON_VALUE = "json";
    final static String FORM_VALUE = "form";
    final static String ROW_VALUE = "row";

    void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target);
}
