package com.github.aqiu202.ideayapi.parser.type;

import com.github.aqiu202.ideayapi.parser.base.DeprecatedAssert;
import com.github.aqiu202.ideayapi.util.CollectionUtils;
import com.github.aqiu202.ideayapi.util.PsiAnnotationUtils;
import com.github.aqiu202.ideayapi.util.PsiDocUtils;
import com.github.aqiu202.ideayapi.util.StringUtils;
import com.intellij.psi.*;

import java.util.*;

public class SimplePsiDescriptor implements PsiDescriptor {

    private final List<PsiModifierListOwner> elements;
    private final String name;
    private final PsiType type;
    private final boolean valid;
    private String description;
    private final Map<String, List<PsiAnnotation>> annotationsMap = new HashMap<>();

    public SimplePsiDescriptor(PsiModifierListOwner element, String name, PsiType type, boolean valid) {
        this.elements = new ArrayList<>(Collections.singletonList(element));
        this.name = name;
        this.type = type;
        this.valid = valid;
    }

    public SimplePsiDescriptor(PsiParameter psiParameter, PsiType paramType, String name) {
        this.elements = new ArrayList<>(Collections.singletonList(psiParameter));
        this.name = name;
        // 传递泛型解析后的参数
        this.type = paramType;
        this.valid = false;
    }

    @Override
    public List<PsiModifierListOwner> getElements() {
        return elements;
    }

    @Override
    public void addElement(PsiModifierListOwner element) {
        this.elements.add(element);
        this.description = null;
        this.annotationsMap.clear();
    }

    @Override
    public void addElement(int index, PsiModifierListOwner element) {
        this.elements.add(0, element);
        this.description = null;
        this.annotationsMap.clear();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PsiType getType() {
        return type;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public String getDescription() {
        if (this.description == null) {
            this.description = resolveDescription();
        }
        return this.description;
    }

    protected String resolveDescription() {
        List<PsiModifierListOwner> elements = this.getElements();
        for (PsiModifierListOwner element : elements) {
            String result = null;
            if (element instanceof PsiMethod) {
                result = this.getDescription(((PsiMethod) element));
            } else if (element instanceof PsiField) {
                result = this.getDescription(((PsiField) element));
            }
            if (StringUtils.isNotBlank(result)) {
                return result;
            }
        }
        return "";
    }

    @Override
    public boolean isDeprecated() {
        return this.elements.stream().anyMatch(DeprecatedAssert.instance::isDeprecated);
    }

    @Override
    public boolean hasAnnotation(String annotationName) {
        return this.findFirstAnnotation(annotationName) != null;
    }

    @Override
    public PsiAnnotation findFirstAnnotation(String annotationName) {
        List<PsiAnnotation> annotations = this.findAnnotations(annotationName);
        if (CollectionUtils.isEmpty(annotations)) {
            return null;
        }
        return annotations.get(0);
    }

    @Override
    public List<PsiAnnotation> findAnnotations(String annotationName) {
        return this.annotationsMap.compute(annotationName, (k, v) -> {
            if (v == null) {
                v = new ArrayList<>();
                for (PsiModifierListOwner element : this.elements) {
                    PsiAnnotation annotation = PsiAnnotationUtils.findAnnotation(element, annotationName);
                    if (annotation != null) {
                        v.add(annotation);
                    }
                }
            }
            return v;
        });
    }

    protected String getDescription(PsiField field) {
        return PsiDocUtils.getComment(field);
    }

    protected String getDescription(PsiMethod method) {
        return PsiDocUtils.getParamComment(method, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplePsiDescriptor that = (SimplePsiDescriptor) o;
        return valid == that.valid && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, valid);
    }
}
