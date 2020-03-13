package com.redsoft.idea.plugin.yapi.model;

/**
 * @description 接口状态
 * @author aqiu
 * @date 2019/7/31
 */
public enum YApiStatusEnum {

    done("已完成"),
    undone("开发中");

    private String message;

    YApiStatusEnum(String message) {
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
