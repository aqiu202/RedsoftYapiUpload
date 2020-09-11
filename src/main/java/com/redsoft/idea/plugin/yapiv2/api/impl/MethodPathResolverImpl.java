package com.redsoft.idea.plugin.yapiv2.api.impl;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.api.abs.AbstractPathResolver;
import com.redsoft.idea.plugin.yapiv2.constant.SpringMVCConstants;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.util.PathUtils;
import com.redsoft.idea.plugin.yapiv2.util.PsiAnnotationUtils;
import java.util.LinkedHashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

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
                    String p = classPath + PathUtils.pathFormat(path, Strings.isBlank(classPath));
                    pathSet.add(p);
                }
            }
            target.setPaths(pathSet);
        }
    }

}
