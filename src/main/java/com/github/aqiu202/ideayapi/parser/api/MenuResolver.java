package com.github.aqiu202.ideayapi.parser.api;

import com.github.aqiu202.ideayapi.model.YApiParam;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;

/**
 * <b>接口分类信息解析</b>
 *
 * @author aqiu 2020/7/23 3:38 下午
 **/
public interface MenuResolver {

    void set(@NotNull PsiClass c, @NotNull YApiParam target);

}
