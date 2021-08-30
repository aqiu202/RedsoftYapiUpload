package com.redsoft.idea.plugin.yapiv2.util;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiModifierListOwner;
import com.redsoft.idea.plugin.yapiv2.constant.AnnotationConstants;
import java.util.Objects;
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
            @NotNull String annotationFQN) {
        return findAnnotationQuick(psiModifierListOwner.getModifierList(), annotationFQN);
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
    public static PsiAnnotation findAnnotation(@NotNull PsiModifierListOwner psiModifierListOwner,
            @NotNull String... annotationFQNs) {
        return findAnnotationQuick(psiModifierListOwner.getModifierList(), annotationFQNs);
    }

    @Nullable
    private static PsiAnnotation findAnnotationQuick(@Nullable PsiModifierList annotationOwner,
            @NotNull String... qualifiedNames) {
        if (annotationOwner == null) {
            return null;
        }
        for (String qualifiedName : qualifiedNames) {
            PsiAnnotation annotation = annotationOwner.findAnnotation(qualifiedName);
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
     * @param psiParameter psiParameter
     * @param annotationName annotationName
     */
    public static String getPsiParameterAnnotationValue(PsiModifierListOwner psiParameter,
            String annotationName) {
        return getPsiParameterAnnotationParam(psiParameter, annotationName,
                PsiAnnotation.DEFAULT_REFERENCED_METHOD_NAME);
    }

    /**
     * 获取注解某个值
     *
     * @param psiParameter psiParameter
     * @param annotationName annotationName
     */
    @Nullable
    public static String getPsiParameterAnnotationParam(PsiModifierListOwner psiParameter,
            String annotationName, String paramName) {
        PsiAnnotation annotation = PsiAnnotationUtils
                .findAnnotation(psiParameter, annotationName);
        if (annotation == null) {
            return null;
        }
        return Objects.requireNonNull(annotation.findAttributeValue(paramName)).getText();
    }

    @Nullable
    public static String getPsiAnnotationAttributeValue(PsiAnnotation annotation,
            String attributeName) {
        PsiAnnotationMemberValue consumes = annotation.findAttributeValue(attributeName);
        return Objects.isNull(consumes) ? null : consumes.getText();

    }
}
