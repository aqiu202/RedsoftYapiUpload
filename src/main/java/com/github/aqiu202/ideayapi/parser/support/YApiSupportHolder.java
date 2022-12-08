package com.github.aqiu202.ideayapi.parser.support;

import com.github.aqiu202.ideayapi.parser.support.jackson.YApiJacksonSupport;
import com.github.aqiu202.ideayapi.parser.support.spring.YApiSpringSupport;
import com.github.aqiu202.ideayapi.parser.support.swagger.YApiSwaggerSupport;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiConstantEvaluationHelper;

/**
 * 当前支持的所有扩展
 */
public abstract class YApiSupportHolder {

    public static Project project;

    public static YApiSupport supports;

    public static PsiConstantEvaluationHelper evaluationHelper;

    public static void init(Project project) {
        YApiSupportHolder.project = project;
        YApiSupportHolder.supports = new YApiSupports(
                YApiSpringSupport.INSTANCE,
                YApiJacksonSupport.INSTANCE,
                YApiSwaggerSupport.INSTANCE);
        YApiSupportHolder.evaluationHelper = JavaPsiFacade.getInstance(project).getConstantEvaluationHelper();
    }
}
