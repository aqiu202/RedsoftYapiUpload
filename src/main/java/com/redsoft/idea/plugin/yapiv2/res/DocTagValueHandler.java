package com.redsoft.idea.plugin.yapiv2.res;

import com.jgoodies.common.base.Strings;

/**
 * <b>注释值的处理</b>
 * @author aqiu
 * @date 2020/7/23 3:57 下午
**/
public interface DocTagValueHandler {

    /**
     * <b>处理注释的值</b>
     * @param value 注释值
     */
    default String handleDocTagValue(String value) {
        if (Strings.isBlank(value)) {
            return value;
        }
        //使用正则替换掉重量级的Jsoup
        return value.replaceAll("<[/\\w]+>", "");
    }
}
