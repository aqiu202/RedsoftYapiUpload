package com.redsoft.idea.plugin.yapiv2.model;

import org.apache.commons.lang3.StringUtils;

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
        if (StringUtils.isNotBlank(valueWrapper.getName())) {
            this.setName(valueWrapper.getName());
        }
        if (StringUtils.isNotBlank(valueWrapper.getDesc())) {
            this.setDesc(valueWrapper.getDesc());
        }
        if (StringUtils.isNotBlank(valueWrapper.getExample())) {
            this.setExample(valueWrapper.getExample());
        }
        if (StringUtils.isNotBlank(valueWrapper.getTypeDesc())) {
            this.setTypeDesc(valueWrapper.getTypeDesc());
        }

    }

}
