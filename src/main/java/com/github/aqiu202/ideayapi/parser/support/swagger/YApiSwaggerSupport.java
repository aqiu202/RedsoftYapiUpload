package com.github.aqiu202.ideayapi.parser.support.swagger;

import com.github.aqiu202.ideayapi.constant.SwaggerConstants;
import com.github.aqiu202.ideayapi.model.ValueWrapper;
import com.github.aqiu202.ideayapi.model.YApiParam;
import com.github.aqiu202.ideayapi.parser.support.YApiSupport;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NonNls;

/**
 * 对Swagger注解的支持
 */
public class YApiSwaggerSupport implements YApiSupport {


    private YApiSwaggerSupport() {
    }

    public static final YApiSwaggerSupport INSTANCE = new YApiSwaggerSupport();

    @Override
    public void handleMenu(PsiClass psiClass, YApiParam apiParam) {
        PsiAnnotation annotation = PsiAnnotationUtils.findAnnotation(psiClass, SwaggerConstants.API);
        if (annotation != null) {
            String value = PsiAnnotationUtils.getPsiAnnotationAttributeValue(annotation, "tags");
            if (StringUtils.isBlank(value)) {
                value = PsiAnnotationUtils.getPsiAnnotationAttributeValue(annotation);
            }
            if (StringUtils.isNotBlank(value)) {
                apiParam.setMenu(value);
            }
            String description = PsiAnnotationUtils.getPsiAnnotationAttributeValue(annotation, "description");
            if (StringUtils.isNotBlank(description)) {
                apiParam.setMenuDesc(description);
            }
        }
    }

    @Override
    public void handleMethod(PsiMethod psiMethod, @NonNls YApiParam apiDTO) {
        String title = PsiAnnotationUtils
                .getPsiAnnotationAttributeValue(psiMethod, SwaggerConstants.API_OPERATION);
        if (StringUtils.isNotBlank(title)) {
            apiDTO.setTitle(title);
        }
    }

    @Override
    public void handleParam(@NonNls ValueWrapper wrapper) {
        PsiAnnotation annotation = wrapper.getSource().findFirstAnnotation(SwaggerConstants.API_PARAM);
        if (annotation != null) {
            String desc = PsiAnnotationUtils.getPsiAnnotationAttributeValue(annotation);
            if (StringUtils.isNotBlank(desc)) {
                wrapper.setDesc(desc);
            }
            String example = PsiAnnotationUtils.getPsiAnnotationAttributeValue(annotation, "example");
            if (StringUtils.isNotBlank(example)) {
                wrapper.setExample(example);
            }
        }
    }

    @Override
    public void handleProperty(@NonNls ValueWrapper wrapper) {
        PsiAnnotation annotation = wrapper.getSource().findFirstAnnotation(SwaggerConstants.API_MODEL_PROPERTY);
        if (annotation != null) {
            String desc = PsiAnnotationUtils
                    .getPsiAnnotationAttributeValue(annotation);
            if (StringUtils.isNotBlank(desc)) {
                wrapper.setDesc(desc);
            }
            String example = PsiAnnotationUtils.getPsiAnnotationAttributeValue(annotation, "example");
            if (StringUtils.isNotBlank(example)) {
                wrapper.setExample(example);
            }
        }

    }

}
