package com.redsoft.idea.plugin.yapiv2.req.impl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.redsoft.idea.plugin.yapiv2.base.ContentTypeResolver;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.util.PsiParamUtils;
import org.jetbrains.annotations.NotNull;

public class RequestContentTypeResolverImpl implements ContentTypeResolver {

    @Override
    public void resolve(@NotNull PsiClass c, @NotNull PsiMethod m, @NotNull YApiParam target) {
        if (PsiParamUtils.noBody(target.getMethod())) {
            return;
        }
//        if (isResponseJson(c, m)) {
        if (!PsiParamUtils.hasRequestBody(m.getParameterList().getParameters())) {
            target.setReq_body_type(FORM_VALUE);
        }
//        } else {
//            target.setReq_body_type(ROW_VALUE);
//        }
//        String consumes = target.getConsumes();
//        if (Strings.isNotBlank(consumes)) {
//            if (JSON.equals(consumes)) {
//                target.setConsumes(consumes);
//            }
//        }
    }

}
