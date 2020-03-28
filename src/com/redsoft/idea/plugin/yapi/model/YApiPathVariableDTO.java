package com.redsoft.idea.plugin.yapi.model;

import java.util.Objects;

/**
 * PathVariable 请求参数
 *
 * @date 2019/5/24 2:24 PM
 */
@SuppressWarnings("unused")
public class YApiPathVariableDTO {

    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String desc;
    /**
     * 示例
     */
    private String example;

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

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public void full(ValueWrapper valueWrapper) {
        if (Objects.nonNull(valueWrapper.getName())) {
            this.setName(valueWrapper.getName());
        }
        if (Objects.nonNull(valueWrapper.getDesc())) {
            this.setDesc(valueWrapper.getDesc());
        }
        if (Objects.nonNull(valueWrapper.getExample())) {
            this.setExample(valueWrapper.getExample());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        YApiPathVariableDTO that = (YApiPathVariableDTO) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
