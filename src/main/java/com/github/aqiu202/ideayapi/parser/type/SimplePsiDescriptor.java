package com.github.aqiu202.ideayapi.parser.type;

import com.github.aqiu202.ideayapi.parser.base.DeprecatedAssert;
import com.github.aqiu202.ideayapi.util.*;
import com.intellij.psi.*;

import java.util.*;

public class SimplePsiDescriptor implements PsiDescriptor {

    private final List<PsiModifierListOwner> elements;
    private final String name;
    private final PsiType type;
    private final boolean valid;
    private String description;
    private final Map<String, List<PsiAnnotation>> annotationsMap = new HashMap<>();

    public SimplePsiDescriptor(PsiModifierListOwner elements) {
        this.elements = new ArrayList<>(Collections.singletonList(elements));
        this.name = null;
        this.type = null;
        this.valid = false;
    }

    public static SimplePsiDescriptor of(PsiModifierListOwner origin) {
        if (origin instanceof PsiField) {
            return new SimplePsiDescriptor(((PsiField) origin));
        }
        if (origin instanceof PsiMethod) {
            return new SimplePsiDescriptor(((PsiMethod) origin));
        }
        return new SimplePsiDescriptor(origin);
    }

    public SimplePsiDescriptor(PsiField element) {
        this.elements = new ArrayList<>(Collections.singletonList(element));
        this.name = element.getName();
        this.type = element.getType();
        this.valid = true;
    }

    public SimplePsiDescriptor(PsiMethod element) {
        this.elements = new ArrayList<>(Collections.singletonList(element));
        String methodName = element.getName();
        String prefix;
        if (methodName.startsWith("set")) {
            prefix = "set";
            PsiParameter firstParameter = element.getParameterList().getParameters()[0];
            this.type = firstParameter == null ? null : firstParameter.getType();
            this.valid = type != null && element.getParameterList().getParametersCount() == 1;
        } else {
            this.type = element.getReturnType();
            prefix = StringUtils.equals("boolean", TypeUtils.getTypePkName(type)) ? "is" : "get";
            this.valid = methodName.startsWith(prefix) && element.getParameterList().isEmpty();
        }
        if (this.valid) {
            methodName = methodName.substring(prefix.length());
            this.name = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
        } else {
            this.name = methodName;
        }
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
