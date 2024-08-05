package com.github.aqiu202.ideayapi.util;

import com.github.aqiu202.ideayapi.constant.AnnotationConstants;
import com.github.aqiu202.ideayapi.parser.support.YApiSupportHolder;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Psi注解读取工具类
 */
public final class PsiAnnotationUtils {

//    private static final Key<String> LOMBOK_ANNOTATION_FQN_KEY = Key
//            .create("LOMBOK_ANNOTATION_FQN");

    @Nullable
    public static PsiAnnotation findAnnotation(@NotNull PsiModifierListOwner psiModifierListOwner,
                                               @NotNull String... annotationFQNs) {
        PsiAnnotation result = findAnnotationQuick(psiModifierListOwner.getModifierList(), annotationFQNs);
        if (result == null) {
            // 字段上没有注解时尝试获取getter方法上的注解
            if (psiModifierListOwner instanceof PsiField) {
                PsiField field = (PsiField) psiModifierListOwner;
                String name = PropertyNamingUtils.upperCamel(field.getName());
                String prefix = "boolean".equals(TypeUtils.getTypePkName(field.getType())) ? "is" : "get";
                PsiMethod[] methods = ((PsiClass) field.getParent()).findMethodsByName(prefix + name, true);
                for (PsiMethod method : methods) {
                    if (method.getParameterList().getParametersCount() == 0) {
                        result = findAnnotation(method, annotationFQNs);
                    }
                }
            }
        }
        return result;
    }

    @Nullable
    private static PsiAnnotation findAnnotationQuick(@Nullable PsiModifierList annotationOwner,
                                                     @NotNull String qualifiedName) {
        if (annotationOwner == null) {
            return null;
        }
        return annotationOwner.findAnnotation(qualifiedName);
    }

    @Nullable
    private static PsiAnnotation findAnnotationQuick(@Nullable PsiModifierList annotationOwner,
                                                     @NotNull String... qualifiedNames) {
        if (annotationOwner == null) {
            return null;
        }
        for (String qualifiedName : qualifiedNames) {
            PsiAnnotation annotation = findAnnotationQuick(annotationOwner, qualifiedName);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }

    public static boolean hasDeprecated(@NotNull PsiModifierListOwner psiModifierListOwner) {
        return null != findAnnotation(psiModifierListOwner, AnnotationConstants.Deprecated);
    }

//    @Nullable
//    private static String getAndCacheFQN(@NotNull PsiAnnotation annotation,
//            @Nullable String referenceName) {
//        String annotationQualifiedName = annotation.getCopyableUserData(LOMBOK_ANNOTATION_FQN_KEY);
//        if (null == annotationQualifiedName || (null != referenceName && !annotationQualifiedName
//                .endsWith(".".concat(referenceName)))) {
//            annotationQualifiedName = annotation.getQualifiedName();
//            if (null != annotationQualifiedName && annotationQualifiedName.indexOf('.') > -1) {
//                annotation.putCopyableUserData(LOMBOK_ANNOTATION_FQN_KEY, annotationQualifiedName);
//            }
//        }
//        return annotationQualifiedName;
//    }

    public static boolean isAnnotatedWith(@NotNull PsiModifierListOwner psiModifierListOwner,
                                          @NotNull String annotationFQN) {
        return null != findAnnotation(psiModifierListOwner, annotationFQN);
    }

    public static boolean isNotAnnotatedWith(@NotNull PsiModifierListOwner psiModifierListOwner,
                                             @NotNull String annotationFQN) {
        return null == findAnnotation(psiModifierListOwner, annotationFQN);
    }

    /**
     * 获取psi注解value
     *
     * @param psiParameter   psiParameter
     * @param annotationName annotationName
     */
    public static String getPsiAnnotationAttributeValue(PsiModifierListOwner psiParameter,
                                                        String annotationName) {
        return getPsiAnnotationAttributeValue(psiParameter, annotationName,
                PsiAnnotation.DEFAULT_REFERENCED_METHOD_NAME);
    }

    /**
     * 获取注解某个值
     *
     * @param psiParameter   psiParameter
     * @param annotationName annotationName
     */
    @Nullable
    public static String getPsiAnnotationAttributeValue(PsiModifierListOwner psiParameter,
                                                        String annotationName, String attributeName) {
        PsiAnnotation annotation = PsiAnnotationUtils
                .findAnnotation(psiParameter, annotationName);
        if (annotation == null) {
            return null;
        }
        return getPsiAnnotationAttributeValue(annotation, attributeName);
    }

//    public static String getPsiAnnotationAttributeValue(PsiAnnotation psiAnnotation, String attributeName) {
//        JvmAnnotationAttribute attribute = psiAnnotation.findAttribute(attributeName);
//        if (attribute != null) {
//            JvmAnnotationAttributeValue attributeValue = attribute.getAttributeValue();
//            if (attributeValue instanceof )
//        }
//        return null;
//    }

    @Nullable
    public static String getPsiAnnotationAttributeValue(PsiAnnotation annotation,
                                                        String attributeName) {
        PsiAnnotationMemberValue attributeValue = annotation.findAttributeValue(attributeName);
        if (attributeValue != null) {
            Object result = YApiSupportHolder.evaluationHelper.computeConstantExpression(attributeValue);
            if (result != null) {
                return resolveAnnotationValue(result.toString());
            } else {
                return attributeValue.getText();
            }
        }
        return null;
    }

    @Nullable
    public static String getPsiAnnotationAttributeValue(PsiAnnotation annotation) {
        return getPsiAnnotationAttributeValue(annotation, PsiAnnotation.DEFAULT_REFERENCED_METHOD_NAME);
    }

    public static String resolveAnnotationValue(String value) {
        return StringUtils.isBlank(value) ? value : value.replace("\"", "");
    }
}
