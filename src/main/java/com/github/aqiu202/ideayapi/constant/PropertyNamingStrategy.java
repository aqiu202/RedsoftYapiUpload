package com.github.aqiu202.ideayapi.constant;

public enum PropertyNamingStrategy {
    /**
     * 单词之间用'-'分割 --1
     */
    KebabCase,
    /**
     * 单词之间用'_'分割 --2
     */
    SnakeCase,
    /**
     * 全部转化为小写 --3
     */
    LowerCase,
    /**
     * 大驼峰规则 --4
     */
    UpperCamelCase,
    /**
     * 不做处理 --0
     */
    None;

    public static PropertyNamingStrategy of(String name) {
        switch (name.toLowerCase()) {
            case "1":
            case "kebabcase":
                return KebabCase;
            case "2":
            case "snakecase":
                return SnakeCase;
            case "3":
            case "lowercase":
                return LowerCase;
            case "4":
            case "uppercamelcase":
                return UpperCamelCase;
            default:
                return None;
        }
    }
}
