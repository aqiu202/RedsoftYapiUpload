package com.redsoft.idea.plugin.yapi.support.swagger;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapi.constant.SwaggerConstants;
import com.redsoft.idea.plugin.yapi.model.ValueWrapper;
import com.redsoft.idea.plugin.yapi.model.YApiDTO;
import com.redsoft.idea.plugin.yapi.support.YApiSupport;
import com.redsoft.idea.plugin.yapi.util.PsiAnnotationSearchUtil;
import org.jetbrains.annotations.NonNls;

public class YApiSwaggerSupport implements YApiSupport {

    @Override
    public boolean isImportant() {
        return true;
    }

    @Override
    public void handleMethod(PsiMethod psiMethod, @NonNls YApiDTO apiDTO) {
        if (Strings.isNotBlank(apiDTO.getTitle())) {
            String title = PsiAnnotationSearchUtil
                    .getPsiParameterAnnotationValue(psiMethod, SwaggerConstants.API_OPERATION);
            apiDTO.setTitle(title);
        }
    }

    @Override
    public void handleParam(PsiParameter psiParameter, @NonNls ValueWrapper wrapper) {

    }

    @Override
    public void handleField(PsiField psiField, @NonNls ValueWrapper wrapper) {

    }
}
