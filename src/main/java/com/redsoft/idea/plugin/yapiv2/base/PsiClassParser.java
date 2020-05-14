package com.redsoft.idea.plugin.yapiv2.base;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * <b>类解析</b>
 * @author aqiu
 * @date 2020/5/12 11:02 上午
 **/
public interface PsiClassParser {

    List<YApiParam> parse(@NotNull PsiClass c);

    List<PsiMethod> getApiMethods(@NotNull PsiClass c);
}
