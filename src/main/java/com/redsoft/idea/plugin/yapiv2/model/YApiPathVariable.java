package com.redsoft.idea.plugin.yapiv2.model;

import java.util.Objects;

/**
 * PathVariable 请求参数
 *
 * @date 2019/5/24 2:24 PM
 */
@SuppressWarnings("unused")
public class YApiPathVariable extends ValueWrapper {

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
        YApiPathVariable that = (YApiPathVariable) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
