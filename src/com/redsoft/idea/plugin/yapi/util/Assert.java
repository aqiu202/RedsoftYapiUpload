package com.redsoft.idea.plugin.yapi.util;

public class Assert {

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(String str, String message) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasText(String str, String message) {
        if (str == null || str.replace(" ", "").length() == 0) {
            throw new IllegalArgumentException(message);
        }
    }
}
