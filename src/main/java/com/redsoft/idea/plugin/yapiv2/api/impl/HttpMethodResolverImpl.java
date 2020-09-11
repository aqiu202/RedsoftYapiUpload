package com.redsoft.idea.plugin.yapiv2.api.impl;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.api.HttpMethodResolver;
import com.redsoft.idea.plugin.yapiv2.constant.HttpMethodConstants;
import com.redsoft.idea.plugin.yapiv2.constant.SpringMVCConstants;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.util.PsiAnnotationUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class HttpMethodResolverImpl implements HttpMethodResolver {

    @Override
    public void resolve(@NotNull PsiMethod method, @NotNull YApiParam target) {
        Set<String> methods = new LinkedHashSet<>();
        //获取方法上的RequestMapping注解
        PsiAnnotation annotation = PsiAnnotationUtils
                .findAnnotation(method, SpringMVCConstants.RequestMapping);
        if (annotation != null) {
            PsiAnnotationMemberValue m = annotation.findAttributeValue("method");
            if (m != null) {
                methods.addAll(this.processMethod(m.getText().toUpperCase()));
            }
        } else if (PsiAnnotationUtils
                .isAnnotatedWith(method, SpringMVCConstants.GetMapping)) {
            methods.add(HttpMethodConstants.GET);
        } else if (PsiAnnotationUtils
                .isAnnotatedWith(method, SpringMVCConstants.PostMapping)) {
            methods.add(HttpMethodConstants.POST);
        } else if (PsiAnnotationUtils
                .isAnnotatedWith(method, SpringMVCConstants.PutMapping)) {
            methods.add(HttpMethodConstants.PUT);
        } else if (PsiAnnotationUtils
                .isAnnotatedWith(method, SpringMVCConstants.DeleteMapping)) {
            methods.add(HttpMethodConstants.DELETE);
        } else if (PsiAnnotationUtils
                .isAnnotatedWith(method, SpringMVCConstants.PatchMapping)) {
            methods.add(HttpMethodConstants.PATCH);
        }
        target.setMethods(methods);
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
