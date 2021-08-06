package com.redsoft.idea.plugin.yapiv2.req;

import com.intellij.psi.PsiParameter;
import java.util.function.Predicate;

/**
 * <b>请求参数的过滤</b>
 * @author aqiu 2020/7/23 3:59 下午
 **/
@FunctionalInterface
public interface PsiParamFilter extends Predicate<PsiParameter> {

}
