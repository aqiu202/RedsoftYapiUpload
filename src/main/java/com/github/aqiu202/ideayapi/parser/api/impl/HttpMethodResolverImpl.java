package com.github.aqiu202.ideayapi.parser.api.impl;

import com.github.aqiu202.ideayapi.constant.HttpMethodConstants;
import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.api.HttpMethodResolver;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class HttpMethodResolverImpl implements HttpMethodResolver {

    @Override
    public void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
        this.resolveWithClass(c, target);
        this.resolveWithMethod(m, target);
    }

    protected void resolveWithClass(PsiClass c, YApiParam target) {
        Set<String> methods = target.getMethods();
        if (methods == null) {
            methods = new LinkedHashSet<>();
            if (PsiAnnotationUtils
                    .isAnnotatedWith(c, SpringMVCConstants.GetMapping)) {
                methods.add(HttpMethodConstants.GET);
            } else if (PsiAnnotationUtils
                    .isAnnotatedWith(c, SpringMVCConstants.PostMapping)) {
                methods.add(HttpMethodConstants.POST);
            } else if (PsiAnnotationUtils
                    .isAnnotatedWith(c, SpringMVCConstants.PutMapping)) {
                methods.add(HttpMethodConstants.PUT);
            } else if (PsiAnnotationUtils
                    .isAnnotatedWith(c, SpringMVCConstants.DeleteMapping)) {
                methods.add(HttpMethodConstants.DELETE);
            } else if (PsiAnnotationUtils
                    .isAnnotatedWith(c, SpringMVCConstants.PatchMapping)) {
                methods.add(HttpMethodConstants.PATCH);
            }
            if (!methods.isEmpty()) {
                target.setMethods(methods);
            }
        }
    }

    protected void resolveWithMethod(PsiMethod m, YApiParam target) {
        Set<String> methods = target.getMethods();
        if (methods == null) {
            methods = new LinkedHashSet<>();
            //获取方法上的RequestMapping注解
            PsiAnnotation annotation = PsiAnnotationUtils
                    .findAnnotation(m, SpringMVCConstants.RequestMapping);
            if (annotation != null) {
                String methodVal = PsiAnnotationUtils.getPsiAnnotationAttributeValue(annotation, "method");
                if (StringUtils.isNotBlank(methodVal)) {
                    methods.addAll(this.processMethod(methodVal.toUpperCase()));
                }
            } else if (PsiAnnotationUtils
                    .isAnnotatedWith(m, SpringMVCConstants.GetMapping)) {
                methods.add(HttpMethodConstants.GET);
            } else if (PsiAnnotationUtils
                    .isAnnotatedWith(m, SpringMVCConstants.PostMapping)) {
                methods.add(HttpMethodConstants.POST);
            } else if (PsiAnnotationUtils
                    .isAnnotatedWith(m, SpringMVCConstants.PutMapping)) {
                methods.add(HttpMethodConstants.PUT);
            } else if (PsiAnnotationUtils
                    .isAnnotatedWith(m, SpringMVCConstants.DeleteMapping)) {
                methods.add(HttpMethodConstants.DELETE);
            } else if (PsiAnnotationUtils
                    .isAnnotatedWith(m, SpringMVCConstants.PatchMapping)) {
                methods.add(HttpMethodConstants.PATCH);
            }
            if (methods.isEmpty()) {
                target.setMethods(new LinkedHashSet<>(Arrays.asList(HttpMethodConstants.GET,
                        HttpMethodConstants.POST, HttpMethodConstants.PUT,
                        HttpMethodConstants.PATCH, HttpMethodConstants.DELETE)));
            } else {
                target.setMethods(methods);
            }
        }
    }

    private Set<String> processMethod(String methodString) {
        //去掉空格
        methodString = methodString.replace(" ", "");
        //如果为空则所有方法都可以访问
        if ("{}".equals(methodString)) {
            return HttpMethodConstants.ALL;
        }
        //如果是集合且不为空
        if (methodString.startsWith("{")) {
            //去除两边的大括号
            methodString = methodString.substring(1, methodString.length() - 1);
            return Arrays.stream(methodString.split(",")).map(this::processMethodItem)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return Collections.singleton(this.processMethodItem(methodString));
    }

    private String processMethodItem(String method) {
        //解决@RequestMapping注解获取不到方法
        return method.substring(method.lastIndexOf(".") + 1);
    }
}
