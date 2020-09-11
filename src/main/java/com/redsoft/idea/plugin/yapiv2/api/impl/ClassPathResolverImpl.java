package com.redsoft.idea.plugin.yapiv2.api.impl;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import com.redsoft.idea.plugin.yapiv2.api.abs.AbstractPathResolver;
import com.redsoft.idea.plugin.yapiv2.constant.SpringMVCConstants;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.util.PathUtils;
import com.redsoft.idea.plugin.yapiv2.util.PsiAnnotationUtils;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class ClassPathResolverImpl extends AbstractPathResolver {

    @Override
    public void resolve(@NotNull PsiModifierListOwner m, @NotNull YApiParam target) {
        //获取类上面的RequestMapping 中的value
        PsiAnnotation psiAnnotation = PsiAnnotationUtils
                .findAnnotation(m, SpringMVCConstants.RequestMapping);
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
                    .collect(Collectors.toSet());
            target.setPaths(pathSet);
        }
    }

}
