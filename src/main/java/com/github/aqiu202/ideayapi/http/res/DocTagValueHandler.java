package com.github.aqiu202.ideayapi.http.res;

import org.apache.commons.lang3.StringUtils;

/**
 * <b>注释值的处理</b>
 *
 * @author aqiu 2020/7/23 3:57 下午
 **/
public interface DocTagValueHandler {

    /**
     * <b>处理注释的值</b>
     *
     * @param value 注释值
     */
    default String handleDocTagValue(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        //使用正则替换掉重量级的Jsoup
        return value.replaceAll("<[/\\w]+>", "");
    }
}
