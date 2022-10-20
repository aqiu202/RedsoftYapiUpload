package com.github.aqiu202.ideayapi.parser.api.impl;

import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.api.HttpMethodResolver;
import com.github.aqiu202.ideayapi.parser.api.abs.AbstractPathResolver;
import com.github.aqiu202.ideayapi.util.PathUtils;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassPathResolverImpl extends AbstractPathResolver {

    private HttpMethodResolver httpMethodResolver = new HttpMethodResolverImpl();

    public HttpMethodResolver getHttpMethodResolver() {
        return httpMethodResolver;
    }

    public void setHttpMethodResolver(HttpMethodResolver httpMethodResolver) {
        this.httpMethodResolver = httpMethodResolver;
    }

    @Override
    public void resolve(@NotNull PsiModifierListOwner m, @NotNull YApiParam target) {
        //处理在类上声明方法的情况
        this.getHttpMethodResolver().resolve(m, target);
        //获取类上面的RequestMapping 中的value，如果路径上使用了
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(m, SpringMVCConstants.RequestMapping, SpringMVCConstants.GetMapping,
                        SpringMVCConstants.PostMapping, SpringMVCConstants.PutMapping,
                        SpringMVCConstants.DeleteMapping, SpringMVCConstants.PatchMapping);
        if (psiAnnotation != null) {
            //暂不处理consumes字段
//            String consumes = PsiAnnotationUtils
//                    .getPsiAnnotationAttributeValue(psiAnnotation, "consumes");
//            target.setConsumes(consumes);
            //解析原始的path数据
            Set<String> paths = this.getPathByAnnotation(psiAnnotation);
            //处理原始数据
            Set<String> pathSet = paths.stream().filter(Objects::nonNull)
                    .map(path -> PathUtils.pathFormat(path, false))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            target.setPaths(pathSet);
        } else {
            target.setPaths(empty);
        }
    }

}
