package com.github.aqiu202.ideayapi.parser.doc;

import com.github.aqiu202.ideayapi.util.StringUtils;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaDocumentedElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JavaDocument {

    public static final String DOC_COMMENT_DATA = "DOC_COMMENT_DATA";

    public static final String DOC_TAG_VALUE_ELEMENT = "DOC_TAG_VALUE_ELEMENT";

    private final Map<String, String> tagValues = new HashMap<>();
    private final Map<String, String> tagTexts = new HashMap<>();
    private final Map<String, String> paramValues = new HashMap<>();
    private String text;

    private static final String PARAM_NAME = "param";

    private final PsiDocComment docComment;

    public JavaDocument(PsiDocComment docComment) {
        this.docComment = docComment;
    }

    public JavaDocument(PsiJavaDocumentedElement element) {
        this(element.getDocComment());
        this.initialize();
    }

    public PsiDocComment getDocComment() {
        return docComment;
    }

    private void initialize() {
        if (this.docComment != null) {
            this.text = Arrays.stream(this.docComment.getChildren())
                    .filter(e -> StringUtils.equals(e.getNode().getElementType().toString(), JavaDocument.DOC_COMMENT_DATA))
                    .map(PsiElement::getText).collect(Collectors.joining("\n"));
            PsiDocTag[] tags = this.docComment.getTags();
            for (PsiDocTag tag : tags) {
                String tagName = tag.getName();
                PsiDocTagValue valueElement = tag.getValueElement();
                if (valueElement == null) {
                    continue;
                }
                String key = tagName.toLowerCase();
                String value = valueElement.getText();
                PsiElement[] dataElements = tag.getDataElements();
                if (StringUtils.equalsIgnoreCase(tagName, PARAM_NAME)) {
                    String paramValue = Arrays.stream(dataElements)
                            .filter(element -> StringUtils.equals(element.getNode().getElementType().toString(), DOC_COMMENT_DATA))
                            .map(PsiElement::getText)
                            .collect(Collectors.joining(" ")).trim();
                    this.paramValues.put(value, paramValue);
                } else {
                    this.tagValues.put(key, value);
                    String text = Arrays.stream(dataElements).map(PsiElement::getText)
                            .collect(Collectors.joining(" ")).trim();
                    this.tagTexts.put(key, text);
                }
            }
        }
    }

    public String getParamValue(String paramName) {
        return this.paramValues.get(paramName);
    }

    public String getTagValue(String tagName) {
        return this.tagValues.get(tagName);
    }

    public String getTagText(String tagName) {
        return this.tagTexts.get(tagName);
    }

    public String getText() {
        return text;
    }
}
