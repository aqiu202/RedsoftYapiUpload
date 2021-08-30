package com.redsoft.idea.plugin.yapiv2.util;

import com.jgoodies.common.base.Strings;
import com.redsoft.idea.plugin.yapiv2.constant.PropertyNamingStrategy;
import org.jetbrains.annotations.NotNull;

/**
 * 字段名称转换工具类
 * @author aqiu202
 */
public final class PropertyNamingUtils {

    private static final char underline = '_';
    private static final char middleLine = '-';

    /**
     * 驼峰转下划线命名
     *
     * @author aqiu 2018年5月16日 下午2:44:14
     * @param propName propName
     */
    public static String underline(String propName) {
        return splitWith(propName, underline);
    }

    public static String middleLine(String propName) {
        return splitWith(propName, middleLine);
    }

    public static String splitWith(String propName, char line) {
        if (Strings.isNotBlank(propName)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < propName.length(); i++) {
                char c = propName.charAt(i);
                if (i > 0 && Character.isUpperCase(c)) {
                    sb.append(line).append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        } else {
            return propName;
        }
    }

    /**
     * 小驼峰转大驼峰
     *
     * @author aqiu 2018年5月16日 下午2:43:49
     * @param propName propName
     */
    public static String upperCamel(String propName) {
        if (Strings.isNotBlank(propName)) {
            return Character.toUpperCase(propName.charAt(0)) + propName.substring(1);
        } else {
            return propName;
        }
    }

    /**
     * 转为小写字母
     * @param propName 字段名称
     */
    public static String lowerCase(String propName) {
        if (Strings.isNotBlank(propName)) {
            return propName.toLowerCase();
        } else {
            return propName;
        }
    }

    public static String convert(String propName, @NotNull PropertyNamingStrategy strategy) {
        switch (strategy) {
            case KebabCase:
                return middleLine(propName);
            case SnakeCase:
            case LowerCaseWithUnderscoresStrategy:
                return underline(propName);
            case LowerCase:
                return lowerCase(propName);
            case UpperCamelCase:
            case PascalCaseStrategy:
                return upperCamel(propName);
            default:
                return propName;
        }
    }

}
