package com.redsoft.idea.plugin.yapiv2.req;

import com.intellij.psi.PsiParameter;
import java.util.function.Predicate;

@FunctionalInterface
public interface PsiParamFilter extends Predicate<PsiParameter> {

}
