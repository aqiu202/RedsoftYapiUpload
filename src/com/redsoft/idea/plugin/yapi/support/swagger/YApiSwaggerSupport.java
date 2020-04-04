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


    private YApiSwaggerSupport() {
    }

    public static final YApiSwaggerSupport INSTANCE = new YApiSwaggerSupport();

    @Override
    public boolean isImportant() {
        return true;
    }

    @Override
    public void handleMethod(PsiMethod psiMethod, @NonNls YApiDTO apiDTO) {
        if (Strings.isBlank(apiDTO.getTitle())) {
            String title = PsiAnnotationSearchUtil
                    .getPsiParameterAnnotationValue(psiMethod, SwaggerConstants.API_OPERATION);
            apiDTO.setTitle(title);
        }
    }

    @Override
    public void handleParam(PsiParameter psiParameter, @NonNls ValueWrapper wrapper) {
        if (Strings.isBlank(wrapper.getDesc())) {
            String desc = PsiAnnotationSearchUtil
                    .getPsiParameterAnnotationValue(psiParameter, SwaggerConstants.API_PARAM);
            wrapper.setDesc(desc);
        }
    }

    @Override
    public void handleField(PsiField psiField, @NonNls ValueWrapper wrapper) {
        if (Strings.isBlank(wrapper.getDesc())) {
            String desc = PsiAnnotationSearchUtil
                    .getPsiParameterAnnotationValue(psiField, SwaggerConstants.API_MODEL_PROPERTY);
            wrapper.setDesc(desc);
        }
    }
}
