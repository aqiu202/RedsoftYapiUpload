package com.redsoft.idea.plugin.yapiv2.res.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.base.ContentTypeResolver;
import com.redsoft.idea.plugin.yapiv2.constant.SpringMVCConstants;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.util.ValidUtils;
import org.jetbrains.annotations.NotNull;

public class ResponseContentTypeResolverImpl implements ContentTypeResolver {

    @Override
    public void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
        if (this.isResponseJson(c, m)) {
            target.setRes_body_type(JSON_VALUE);
        } else {
            target.setRes_body_type(ROW_VALUE);
        }
//        String consumes = target.getConsumes();
//        if (Strings.isNotBlank(consumes)) {
//            if (json.equals(consumes)) {
//                target.setConsumes(consumes);
//            }
//        }
    }

    private boolean isResponseJson(PsiClass psiClass, PsiMethod psiMethod) {
        return ValidUtils.hasAnnotation(psiClass, SpringMVCConstants.RestController) ||
                ValidUtils.hasAnnotation(psiClass, SpringMVCConstants.ResponseBody) ||
                ValidUtils.hasAnnotation(psiMethod, SpringMVCConstants.ResponseBody);
    }

}
