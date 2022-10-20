package com.github.aqiu202.ideayapi.parser.api.impl;

import com.github.aqiu202.ideayapi.constant.HttpMethodConstants;
import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.api.HttpMethodResolver;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class HttpMethodResolverImpl implements HttpMethodResolver {

    @Override
    public void resolve(@NotNull PsiModifierListOwner owner, @NotNull YApiParam target) {
        Set<String> methods = target.getMethods();
        if (methods == null) {
            methods = new LinkedHashSet<>();
            //获取方法上的RequestMapping注解
            PsiAnnotation annotation = PsiAnnotationUtils
                    .findAnnotation(owner, SpringMVCConstants.RequestMapping);
            if (annotation != null) {
                // 类上不处理缺省方法
                if (!(owner instanceof PsiClass)) {
                    PsiAnnotationMemberValue m = annotation.findAttributeValue("method");
                    if (m != null) {
                        methods.addAll(this.processMethod(m.getText().toUpperCase()));
                    }
                }
            } else if (PsiAnnotationUtils
                    .isAnnotatedWith(owner, SpringMVCConstants.GetMapping)) {
                methods.add(HttpMethodConstants.GET);
            } else if (PsiAnnotationUtils
                    .isAnnotatedWith(owner, SpringMVCConstants.PostMapping)) {
                methods.add(HttpMethodConstants.POST);
            } else if (PsiAnnotationUtils
                    .isAnnotatedWith(owner, SpringMVCConstants.PutMapping)) {
                methods.add(HttpMethodConstants.PUT);
            } else if (PsiAnnotationUtils
                    .isAnnotatedWith(owner, SpringMVCConstants.DeleteMapping)) {
                methods.add(HttpMethodConstants.DELETE);
            } else if (PsiAnnotationUtils
                    .isAnnotatedWith(owner, SpringMVCConstants.PatchMapping)) {
                methods.add(HttpMethodConstants.PATCH);
            }
            if (!methods.isEmpty()) {
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
