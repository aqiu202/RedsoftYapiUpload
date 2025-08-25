package com.github.aqiu202.ideayapi.mode.json5;

import com.github.aqiu202.ideayapi.util.StringUtils;

/**
 * <b>注释模式</b>
 *
 * @author aqiu 2020/2/24 11:07 上午
 **/
public enum CommentMode {

    SINGLE("// %s"),
    MULTIPLE("/* %s */");

    private final String template;

    CommentMode(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public CommentMode parse(String mode) {
        if (StringUtils.equalsIgnoreCase(mode, "single")) {
            return SINGLE;
        }
        return MULTIPLE;
    }
}
