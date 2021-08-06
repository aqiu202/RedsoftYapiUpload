package com.redsoft.idea.plugin.yapiv2.model;

/**
 * 接口状态
 * @author aqiu 2019/7/31
 */
public enum YApiStatus {

    done("已完成"),
    undone("开发中");

    private final String message;

    YApiStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static String getStatus(String message) {
        if (done.getMessage().equals(message) || done.name().equals(message)) {
            return done.name();
        }
        return undone.name();
    }
}
