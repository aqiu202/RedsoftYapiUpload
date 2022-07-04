package com.github.aqiu202.ideayapi.model;

import com.github.aqiu202.ideayapi.parser.Jsonable;
import com.intellij.psi.PsiField;

public class FieldValueWrapper {

    private final PsiField field;
    private String fieldName;
    private Jsonable value;
    private String description;

    public FieldValueWrapper(PsiField field) {
        this.field = field;
    }

    public PsiField getField() {
        return field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Jsonable getValue() {
        return value;
    }

    public void setValue(Jsonable value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
