package com.github.aqiu202.ideayapi.model;

import com.intellij.psi.PsiClassType;

public class EnumResult {
    public EnumResult(boolean valid, PsiClassType type) {
        this.valid = valid;
        this.type = type;
    }

    public EnumResult() {
    }

    private boolean valid;

    private PsiClassType type;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public PsiClassType getType() {
        return type;
    }

    public void setType(PsiClassType type) {
        this.type = type;
    }
}
