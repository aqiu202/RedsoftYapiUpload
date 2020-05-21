package com.redsoft.idea.plugin.yapiv2.res;

import com.jgoodies.common.base.Strings;
import org.jsoup.Jsoup;

public interface DocTagValueHandler {

    /**
     * <b>处理注释的值</b>
     * @param value 注释值
     */
    default String handleDocTagValue(String value) {
        if (Strings.isBlank(value)) {
            return value;
        }
        return Jsoup.parseBodyFragment(value).body().text();
    }
}
