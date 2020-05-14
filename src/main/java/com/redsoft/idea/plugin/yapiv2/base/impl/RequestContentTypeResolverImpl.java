package com.redsoft.idea.plugin.yapiv2.base.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.base.ContentTypeResolver;
import com.redsoft.idea.plugin.yapiv2.constant.SpringMVCConstants;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.util.PsiParamUtils;
import com.redsoft.idea.plugin.yapiv2.util.ValidUtils;
import org.jetbrains.annotations.NotNull;

public class RequestContentTypeResolverImpl implements ContentTypeResolver {

    @Override
    public void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
        if (isResponseJson(c, m)) {
            if (PsiParamUtils.hasRequestBody(m.getParameterList().getParameters())) {
                target.setReq_body_type(JSON_VALUE);
            } else {
                target.setReq_body_type(FORM_VALUE);
            }
        } else {
            target.setReq_body_type(ROW_VALUE);
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
