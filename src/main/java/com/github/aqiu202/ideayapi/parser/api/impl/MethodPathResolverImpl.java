package com.github.aqiu202.ideayapi.parser.api.impl;

import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.api.abs.AbstractPathResolver;
import com.github.aqiu202.ideayapi.util.PathUtils;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class MethodPathResolverImpl extends AbstractPathResolver {

    @Override
    public void resolve(@NotNull PsiModifierListOwner m, @NotNull YApiParam target) {
        //获取方法上面的RequestMapping 中的value
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(m, SpringMVCConstants.RequestMapping, SpringMVCConstants.GetMapping,
                        SpringMVCConstants.PostMapping, SpringMVCConstants.PutMapping,
                        SpringMVCConstants.DeleteMapping, SpringMVCConstants.PatchMapping);
        if (psiAnnotation != null) {
            //暂不处理consumes字段
//            String consumes = PsiAnnotationUtils
//                    .getPsiAnnotationAttributeValue(psiAnnotation, "consumes");
//            target.setConsumes(consumes);
            //获取到类上解析到的path集合
            Set<String> classPaths = target.getPaths();
            Set<String> paths = this.getPathByAnnotation(psiAnnotation);
            Set<String> pathSet = new LinkedHashSet<>();
            for (String classPath : classPaths) {
                for (String path : paths) {
                    String p = classPath + PathUtils.pathFormat(path, StringUtils.isBlank(classPath));
                    pathSet.add(p);
                }
            }
            target.setPaths(pathSet);
        }
    }

}
