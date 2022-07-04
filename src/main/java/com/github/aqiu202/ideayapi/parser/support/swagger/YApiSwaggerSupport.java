package com.github.aqiu202.ideayapi.parser.support.swagger;

import com.github.aqiu202.ideayapi.constant.SwaggerConstants;
import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.support.YApiSupport;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.intellij.psi.PsiMethod;
import com.jgoodies.common.base.Strings;
import org.jetbrains.annotations.NonNls;

/**
 * 对Swagger注解的支持
 */
public class YApiSwaggerSupport implements YApiSupport {


    private YApiSwaggerSupport() {
    }

    public static final YApiSwaggerSupport INSTANCE = new YApiSwaggerSupport();

    @Override
    public void handleMethod(PsiMethod psiMethod, @NonNls YApiParam apiDTO) {
        String title = PsiAnnotationUtils
                .getPsiParameterAnnotationValue(psiMethod, SwaggerConstants.API_OPERATION);
        if (Strings.isNotBlank(title)) {
            apiDTO.setTitle(title.replace("\"", ""));
        }
    }

    @Override
    public void handleParam(@NonNls ValueWrapper wrapper) {
        String desc = PsiAnnotationUtils
                .getPsiParameterAnnotationValue(wrapper.getSource(), SwaggerConstants.API_PARAM);
        if (Strings.isNotBlank(desc)) {
            wrapper.setDesc(desc.replace("\"", ""));
        }
    }

    @Override
    public void handleField(@NonNls ValueWrapper wrapper) {
        String desc = PsiAnnotationUtils
                .getPsiParameterAnnotationValue(wrapper.getSource(),
                        SwaggerConstants.API_MODEL_PROPERTY);
        if (Strings.isNotBlank(desc)) {
            wrapper.setDesc(desc.replace("\"", ""));
        }
    }
}
