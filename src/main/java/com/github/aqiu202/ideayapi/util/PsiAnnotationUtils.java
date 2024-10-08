package com.github.aqiu202.ideayapi.util;

import com.github.aqiu202.ideayapi.constant.AnnotationConstants;
import com.github.aqiu202.ideayapi.parser.support.YApiSupportHolder;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiModifierListOwner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Psi注解读取工具类
 */
public final class PsiAnnotationUtils {

//    private static final Key<String> LOMBOK_ANNOTATION_FQN_KEY = Key
//            .create("LOMBOK_ANNOTATION_FQN");

    @Nullable
    public static PsiAnnotation findAnnotation(@NotNull PsiModifierListOwner psiModifierListOwner,
                                               @NotNull String... annotationFQNs) {
        return findAnnotationQuick(psiModifierListOwner.getModifierList(), annotationFQNs);
    }

    public static PsiAnnotation findAnnotation(@NotNull Collection<PsiModifierListOwner> psiModifierListOwners,
                                               @NotNull String... annotationFQNs) {
        for (PsiModifierListOwner psiModifierListOwner : psiModifierListOwners) {
            PsiAnnotation annotation = findAnnotationQuick(psiModifierListOwner.getModifierList(), annotationFQNs);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
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
                                          @NotNull String... annotationFQNs) {
        return null != findAnnotation(psiModifierListOwner, annotationFQNs);
    }

    public static boolean isNotAnnotatedWith(@NotNull PsiModifierListOwner psiModifierListOwner,
                                             @NotNull String... annotationFQNs) {
        return null == findAnnotation(psiModifierListOwner, annotationFQNs);
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
