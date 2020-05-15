package com.redsoft.idea.plugin.yapiv2.api.impl;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.api.HttpMethodResolver;
import com.redsoft.idea.plugin.yapiv2.constant.HttpMethodConstants;
import com.redsoft.idea.plugin.yapiv2.constant.SpringMVCConstants;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.util.PsiAnnotationUtils;
import org.jetbrains.annotations.NotNull;

public class HttpMethodResolverImpl implements HttpMethodResolver {

    @Override
    public void resolve(@NotNull PsiMethod method, @NotNull YApiParam target) {
        String httpMethod = null;
        //获取方法上的RequestMapping注解
        PsiAnnotation annotation = PsiAnnotationUtils
                .findAnnotation(method, SpringMVCConstants.RequestMapping);
        if (annotation != null) {
            PsiAnnotationMemberValue m = annotation.findAttributeValue("method");
            if (m != null) {
                httpMethod = m.getText().toUpperCase();
            }
        } else if (PsiAnnotationUtils
                .isAnnotatedWith(method, SpringMVCConstants.GetMapping)) {
            httpMethod = HttpMethodConstants.GET;
        } else if (PsiAnnotationUtils
                .isAnnotatedWith(method, SpringMVCConstants.PostMapping)) {
            httpMethod = HttpMethodConstants.POST;
        } else if (PsiAnnotationUtils
                .isAnnotatedWith(method, SpringMVCConstants.PutMapping)) {
            httpMethod = HttpMethodConstants.PUT;
        } else if (PsiAnnotationUtils
                .isAnnotatedWith(method, SpringMVCConstants.DeleteMapping)) {
            httpMethod = HttpMethodConstants.DELETE;
        } else if (PsiAnnotationUtils
                .isAnnotatedWith(method, SpringMVCConstants.PatchMapping)) {
            httpMethod = HttpMethodConstants.PATCH;
        }
        target.setMethod(httpMethod);
    }
}
