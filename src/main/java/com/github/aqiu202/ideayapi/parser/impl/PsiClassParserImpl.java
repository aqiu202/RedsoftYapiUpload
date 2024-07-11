package com.github.aqiu202.ideayapi.parser.impl;

import com.github.aqiu202.ideayapi.config.xml.YApiProjectProperty;
import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.http.filter.PsiMethodFilter;
import com.github.aqiu202.ideayapi.http.filter.PsiMethodListFilter;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.PsiClassParser;
import com.github.aqiu202.ideayapi.parser.PsiMethodParser;
import com.github.aqiu202.ideayapi.parser.base.DeprecatedAssert;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PsiClassParserImpl implements PsiClassParser {

    private final PsiMethodParser methodParser;


    public PsiClassParserImpl(YApiProjectProperty property, Project project) {
        this.methodParser = new PsiMethodParserImpl(property, project);
    }

    public PsiClassParserImpl(PsiMethodParser methodParser) {
        this.methodParser = methodParser;
    }

    @Override
    public List<YApiParam> parse(@NotNull PsiClass c) {
        return this.getApiMethods(c).stream().flatMap(m -> methodParser.parse(c, m).stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<PsiMethod> getApiMethods(@NotNull PsiClass c) {
        return this.getPsiMethodListFilter().apply(c);
    }

    @Override
    public PsiMethodListFilter getPsiMethodListFilter() {
        return c ->
                Arrays.stream(c.getAllMethods()).filter(m -> this.getPsiMethodFilter().apply(m, c)).collect(Collectors.toList());
    }

    @Override
    public PsiMethodFilter getPsiMethodFilter() {
        // 过滤构造器、私有方法、没有RequestMapping注解的方法和过时方法
        return (m, c) -> !(m.getName().equals(c.getName()) ||
                m.getModifierList().hasModifierProperty(PsiModifier.PRIVATE) ||
                //过滤没有RequestMapping注解的方法
                PsiAnnotationUtils
                        .findAnnotation(m, SpringMVCConstants.RequestMapping, SpringMVCConstants.GetMapping,
                                SpringMVCConstants.PostMapping, SpringMVCConstants.PutMapping,
                                SpringMVCConstants.DeleteMapping, SpringMVCConstants.PatchMapping) == null ||
                DeprecatedAssert.instance.isDeprecated(m));
    }
}
