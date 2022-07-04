package com.github.aqiu202.ideayapi.http.req.abs;

import com.github.aqiu202.ideayapi.http.filter.PsiParamListFilter;
import com.github.aqiu202.ideayapi.http.req.EachRequestParamResolver;
import com.github.aqiu202.ideayapi.http.req.RequestParamResolver;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

/**
 * <b>简单的参数处理抽象类</b>
 * <p>只需要定义{@link #getPsiParamFilter}方法过滤要处理的参数
 * 和{@link #doResolverItem}方法依次处理各个参数即可</p>
 *
 * @author aqiu 2020/7/23 4:38 下午
 **/
public abstract class AbstractRequestParamResolver implements PsiParamListFilter,
        RequestParamResolver,
        EachRequestParamResolver {

    @Override
    public void resolve(@NotNull PsiMethod m, @NotNull YApiParam target) {
        this.doResolve(m, this.filter(m, target), target);
    }
}
