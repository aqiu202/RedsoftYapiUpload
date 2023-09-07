package com.github.aqiu202.ideayapi.model;

import com.github.aqiu202.ideayapi.parser.Jsonable;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiVariable;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

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


    protected boolean rawDesc = false;

    protected String typeDesc;
    /**
     * 示例
     */
    protected String example;
    /**
     * 参数名字
     */
    protected String name;

    private PsiModifierListOwner source;

    private Jsonable json;

    public PsiModifierListOwner getSource() {
        return source;
    }

    public void setSource(PsiModifierListOwner source) {
        this.source = source;
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

    public boolean isRawDesc() {
        return rawDesc;
    }

    public void setRawDesc(boolean rawDesc) {
        this.rawDesc = rawDesc;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    @Nullable
    public Jsonable getJson() {
        return json;
    }

    public void setJson(@Nullable Jsonable json) {
        this.json = json;
    }

    public void full(ValueWrapper valueWrapper) {
        this.setRawDesc(valueWrapper.isRawDesc());
        if (StringUtils.isNotBlank(valueWrapper.getName())) {
            this.setName(valueWrapper.getName());
        }
        if (StringUtils.isNotBlank(valueWrapper.getDesc())) {
            this.setDesc(valueWrapper.getDesc());
        }
        if (StringUtils.isNotBlank(valueWrapper.getRequired())) {
            this.setRequired(valueWrapper.getRequired());
        }
        if (StringUtils.isNotBlank(valueWrapper.getExample())) {
            this.setExample(valueWrapper.getExample());
        }
        if (StringUtils.isNotBlank(valueWrapper.getTypeDesc())) {
            this.setTypeDesc(valueWrapper.getTypeDesc());
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
