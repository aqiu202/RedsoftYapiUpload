package com.redsoft.idea.plugin.yapiv2.model;

import java.util.Objects;

/**
 * PathVariable 请求参数
 *
 * @author aqiu  2019/5/24 2:24 PM
 */
@SuppressWarnings("unused")
public class YApiPathVariable extends ValueWrapper {

    @Override
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

}
