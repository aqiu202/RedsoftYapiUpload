package com.redsoft.idea.plugin.yapiv2.parser;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import org.jetbrains.annotations.NotNull;

/**
 * <b>方法解析</b>
 * @author aqiu 2020/5/12 11:02 上午
 **/
public interface PsiMethodParser {

    YApiParam parse(@NotNull PsiClass c, @NotNull PsiMethod m);

}
