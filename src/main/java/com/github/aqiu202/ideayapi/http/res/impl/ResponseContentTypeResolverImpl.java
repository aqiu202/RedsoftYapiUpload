package com.github.aqiu202.ideayapi.http.res.impl;

import com.github.aqiu202.ideayapi.constant.SpringMVCConstants;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.base.ContentTypeResolver;
import com.github.aqiu202.ideayapi.util.ValidUtils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

public class ResponseContentTypeResolverImpl implements ContentTypeResolver {

    @Override
    public void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
        if (this.isResponseJson(c, m)) {
            target.setRes_body_type(JSON_VALUE);
        } else {
            target.setRes_body_type(RAW_VALUE);
        }
//        String consumes = target.getConsumes();
//        if (StringUtils.isNotBlank(consumes)) {
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
