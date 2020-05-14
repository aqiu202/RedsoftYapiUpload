package com.redsoft.idea.plugin.yapiv2.constant;

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
     * 同 SnakeCase
     * @deprecated
     */
    LowerCaseWithUnderscoresStrategy,
    /**
     * 全部转化为小写 --3
     */
    LowerCase,
    /**
     * 大驼峰规则 --4
     */
    UpperCamelCase,
    /**
     * 同 UpperCamelCase
     * @deprecated
     */
    PascalCaseStrategy,
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
            case "lowercasewithunderscoresstrategy":
                return LowerCaseWithUnderscoresStrategy;
            case "3":
            case "lowercase":
                return LowerCase;
            case "4":
            case "uppercamelcase":
                return UpperCamelCase;
            case "pascalcasestrategy":
                return PascalCaseStrategy;
            default:
                return None;
        }
    }
}
