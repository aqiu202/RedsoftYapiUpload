package com.github.aqiu202.ideayapi.parser.api;

import com.github.aqiu202.ideayapi.model.YApiParam;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

/**
 * <b>HttpMethod解析</b>
 *
 * @author aqiu 2020/5/12 11:02 上午
 **/
public interface HttpMethodResolver {

    void resolve(@NotNull PsiMethod method, @NotNull YApiParam target);

}
