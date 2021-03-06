package com.redsoft.idea.plugin.yapiv2.model;

import com.intellij.psi.PsiVariable;
import com.jgoodies.common.base.Strings;
import java.util.Objects;

public class ValueWrapper {

    /**
     * 是否必填
     */
    protected String required = "1";

    /**
     * 描述
     */
    protected String desc;
    /**
     * 示例
     */
    protected String example;
    /**
     * 参数名字
     */
    protected String name;

    private PsiVariable origin;

    public PsiVariable getOrigin() {
        return origin;
    }

    public void setOrigin(PsiVariable origin) {
        this.origin = origin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public void full(ValueWrapper valueWrapper) {
        if (Strings.isNotBlank(valueWrapper.getName())) {
            this.setName(valueWrapper.getName());
        }
        if (Strings.isNotBlank(valueWrapper.getDesc())) {
            this.setDesc(valueWrapper.getDesc());
        }
        if (Strings.isNotBlank(valueWrapper.getRequired())) {
            this.setRequired(valueWrapper.getRequired());
        }
        if (Strings.isNotBlank(valueWrapper.getExample())) {
            this.setExample(valueWrapper.getExample());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ValueWrapper)) {
            return false;
        }
        ValueWrapper target = (ValueWrapper) o;
        return this.name.equals(target.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
}
