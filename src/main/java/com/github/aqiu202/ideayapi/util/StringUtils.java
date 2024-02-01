package com.github.aqiu202.ideayapi.util;

import org.apache.commons.lang3.ArrayUtils;

public abstract class StringUtils {

    public static boolean equals(CharSequence str1, CharSequence str2) {
        return org.apache.commons.lang3.StringUtils.equals(str1, str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return org.apache.commons.lang3.StringUtils.equalsIgnoreCase(str1, str2);
    }

    public static boolean equalsAny(CharSequence source, CharSequence... searchStrings) {
        if (ArrayUtils.isNotEmpty(searchStrings)) {
            for (CharSequence next : searchStrings) {
                if (org.apache.commons.lang3.StringUtils.equals(source, next)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean equalsAnyIgnoreCase(CharSequence string, CharSequence... searchStrings) {
        if (ArrayUtils.isNotEmpty(searchStrings)) {
            for (CharSequence next : searchStrings) {
                if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(string, next)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isBlank(CharSequence string) {
        return org.apache.commons.lang3.StringUtils.isBlank(string);
    }

    public static boolean isEmpty(CharSequence string) {
        return org.apache.commons.lang3.StringUtils.isEmpty(string);
    }

    public static boolean isNotBlank(CharSequence string) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(string);
    }

    public static boolean isNotEmpty(CharSequence string) {
        return org.apache.commons.lang3.StringUtils.isNotEmpty(string);
    }

}
