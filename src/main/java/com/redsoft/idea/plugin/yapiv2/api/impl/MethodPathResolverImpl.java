package com.redsoft.idea.plugin.yapiv2.api.impl;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierListOwner;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.api.abs.AbstractPathResolver;
import com.redsoft.idea.plugin.yapiv2.constant.SpringMVCConstants;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.util.PathUtils;
import com.redsoft.idea.plugin.yapiv2.util.PsiAnnotationUtils;
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
            String path = target.getPath();
            if (Strings.isNotBlank(path)) {
                path = path + PathUtils.pathFormat(this.getPathByAnnotation(psiAnnotation), false);
            } else {
                path = PathUtils.pathFormat(this.getPathByAnnotation(psiAnnotation));
            }
            target.setPath(path);
        }
    }

}
