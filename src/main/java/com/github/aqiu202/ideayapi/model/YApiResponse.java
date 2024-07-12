package com.github.aqiu202.ideayapi.model;

import java.io.Serializable;

/**
 * yapi 返回结果
 *
 * @author aqiu 2019/1/31 12:08 PM
 */
public class YApiResponse<T> implements Serializable {

    /**
     * 状态码
     */
    private Integer errcode;
    /**
     * 状态信息
     */
    private String errmsg;
    /**
     * 返回结果
     */
    private T data;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public YApiResponse() {
    }

    public YApiResponse(T data) {
        this.errcode = 0;
        this.errmsg = "success";
        this.data = data;
    }

    public YApiResponse(Integer errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }
}
