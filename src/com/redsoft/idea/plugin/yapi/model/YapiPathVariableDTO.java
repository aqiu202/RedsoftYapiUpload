package com.redsoft.idea.plugin.yapi.model;

import java.util.Objects;

/**
 * PathVariable 请求参数
 *
 * @date 2019/5/24 2:24 PM
 */
public class YapiPathVariableDTO {

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

    public void full(ValueWraper valueWraper) {
        if (Objects.nonNull(valueWraper.getName())) {
            this.setName(valueWraper.getName());
        }
        if (Objects.nonNull(valueWraper.getDesc())) {
            this.setDesc(valueWraper.getDesc());
        }
        if (Objects.nonNull(valueWraper.getExample())) {
            this.setExample(valueWraper.getExample());
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
        YapiPathVariableDTO that = (YapiPathVariableDTO) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
