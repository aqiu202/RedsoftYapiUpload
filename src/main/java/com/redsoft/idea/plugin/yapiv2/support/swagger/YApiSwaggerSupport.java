package com.redsoft.idea.plugin.yapiv2.support.swagger;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.constant.SwaggerConstants;
import com.redsoft.idea.plugin.yapiv2.model.ValueWrapper;
import com.redsoft.idea.plugin.yapiv2.model.YApiParam;
import com.redsoft.idea.plugin.yapiv2.support.YApiSupport;
import com.redsoft.idea.plugin.yapiv2.util.PsiAnnotationUtils;
import org.jetbrains.annotations.NonNls;

public class YApiSwaggerSupport implements YApiSupport {


    private YApiSwaggerSupport() {
    }

    public static final YApiSwaggerSupport INSTANCE = new YApiSwaggerSupport();

    @Override
    public boolean isImportant() {
        return true;
    }

    @Override
    public void handleMethod(PsiMethod psiMethod, @NonNls YApiParam apiDTO) {
        String title = PsiAnnotationUtils
                .getPsiParameterAnnotationValue(psiMethod, SwaggerConstants.API_OPERATION);
        if (Strings.isNotBlank(title)) {
            apiDTO.setTitle(title);
        }

    }

    @Override
    public void handleParam(PsiParameter psiParameter, @NonNls ValueWrapper wrapper) {
        String desc = PsiAnnotationUtils
                .getPsiParameterAnnotationValue(psiParameter, SwaggerConstants.API_PARAM);
        if (Strings.isNotBlank(desc)) {
            wrapper.setDesc(desc);
        }
    }

    @Override
    public void handleField(PsiField psiField, @NonNls ValueWrapper wrapper) {
        String desc = PsiAnnotationUtils
                .getPsiParameterAnnotationValue(psiField, SwaggerConstants.API_MODEL_PROPERTY);
        if (Strings.isNotBlank(desc)) {
            wrapper.setDesc(desc);
        }
    }
}
