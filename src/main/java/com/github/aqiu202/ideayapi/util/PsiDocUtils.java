package com.github.aqiu202.ideayapi.util;

import com.github.aqiu202.ideayapi.parser.doc.JavaDocument;
import com.github.aqiu202.ideayapi.parser.doc.JavaMethodDocument;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Psi注释读取工具类
 */
public final class PsiDocUtils {

    private static final Map<String, JavaDocument> DOCUMENTS = new HashMap<>();

    public static void clearCache() {
        DOCUMENTS.clear();
    }

    /**
     * 通过paramName 获得描述
     *
     * @author aqiu 2019/5/22
     */
    public static String getParamComment(PsiMethod method, String paramName) {
        PsiDocComment docComment = method.getDocComment();
        if (docComment != null) {
            return DOCUMENTS.compute(elementToString(method), (k, v) -> {
                if (v == null) {
                    v = new JavaMethodDocument(method);
                }
                return v;
            }).getParamValue(paramName);
        }
        return "";
    }

    /**
     * 通过paramIndex 获得描述
     *
     * @author aqiu 2019/5/22
     */
    public static String getParamComment(PsiMethod method, int paramIndex) {
        PsiDocComment docComment = method.getDocComment();
        if (docComment != null) {
            return ((JavaMethodDocument) DOCUMENTS.compute(elementToString(method), (k, v) -> {
                if (v == null) {
                    v = new JavaMethodDocument(method);
                }
                return v;
            })).getParamValue(paramIndex);
        }
        return "";
    }

    /**
     * 获得备注
     *
     * @author aqiu 2019/5/18
     */
    public static String getComment(PsiModifierListOwner owner) {
        if (!(owner instanceof PsiJavaDocumentedElement)) {
            return "";
        }
        PsiJavaDocumentedElement element = (PsiJavaDocumentedElement) owner;
        PsiDocComment psiDocComment = element.getDocComment();
        if (Objects.nonNull(psiDocComment)) {
            return DOCUMENTS.compute(elementToString(element), (k, v) -> {
                if (v == null) {
                    v = new JavaDocument(element);
                }
                return v;
            }).getText();
        }
        return "";
    }

    public static String elementToString(PsiElement element) {
        PsiElement parent = element.getParent();
        String className;
        if (parent instanceof PsiClass) {
            className = ((PsiClass) parent).getQualifiedName();
        } else {
            className = parent.toString();
        }
        String name;
        if (element instanceof PsiNamedElement) {
            name = ((PsiNamedElement) element).getName();
        } else {
            name = element.toString();
        }
        String elementString = className + "." + name;
        if (element instanceof PsiMethod) {
            PsiMethod method = (PsiMethod) element;
            PsiParameterList parameterList = method.getParameterList();
            PsiParameter[] parameters = parameterList.getParameters();
            StringJoiner joiner = new StringJoiner(",", "(", ")");
            for (PsiParameter parameter : parameters) {
                String typeText = TypeUtils.getTypeName(parameter.getType());
                joiner.add(typeText);
            }
            elementString += joiner;
        }
        return elementString;
    }

    @Nullable
    public static PsiDocTag getTagByName(@NotNull PsiDocComment docComment, String... names) {
        PsiDocTag tag;
        for (String name : names) {
            if (Objects.nonNull(tag = docComment.findTagByName(name))) {
                return tag;
            }
        }
        return null;
    }

    @Nullable
    public static String getTagValueByName(@NotNull PsiDocComment docComment, String... names) {
        PsiDocTag tag = getTagByName(docComment, names);
        if (Objects.nonNull(tag)) {
            PsiDocTagValue value = tag.getValueElement();
            if (Objects.nonNull(value)) {
                return value.getText();
            } else {
                PsiElement[] dataElements = tag.getDataElements();
                if (dataElements.length > 0) {
                    return dataElements[0].getText();
                }
            }
        }
        return null;
    }

    public static String getTagDescription(@NotNull PsiDocComment docComment) {
        return Stream.of(docComment.getDescriptionElements())
                .map(PsiElement::getText)
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining());
    }

    public static boolean hasTag(@NotNull PsiDocComment docComment, String... names) {
        return Objects.nonNull(getTagByName(docComment, names));
    }
}
