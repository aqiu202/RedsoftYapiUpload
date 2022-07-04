package com.github.aqiu202.ideayapi.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import com.jgoodies.common.base.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Psi注释读取工具类
 */
public final class PsiDocUtils {

    private PsiDocUtils() {
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
        PsiDocTagValue value;
        if (Objects.isNull(tag) || Objects.isNull(value = tag.getValueElement())) {
            return null;
        }
        return value.getText();
    }

    public static String getTagDescription(@NotNull PsiDocComment docComment) {
        return Stream.of(docComment.getDescriptionElements())
                .map(PsiElement::getText)
                .map(String::trim)
                .filter(Strings::isNotBlank)
                .collect(Collectors.joining());
    }

    public static boolean hasTag(@NotNull PsiDocComment docComment, String... names) {
        return Objects.nonNull(getTagByName(docComment, names));
    }
}
