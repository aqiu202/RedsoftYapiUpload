package com.redsoft.idea.plugin.yapi.model;

import java.io.Serializable;
import java.util.Set;

/**
 * yapi dto
 *
 * @date 2019/2/11 3:16 PM
 */
@SuppressWarnings("unused")
public class YApiDTO implements Serializable {

    /**
     * 路径
     */
    private String path = "/";
    /**
     * 请求Query参数
     */
    private Set<YApiQueryDTO> params;
    /**
     * 头信息
     */
    private Set<YApiHeaderDTO> header;
    /**
     * title
     */
    private String title;
    /**
     * 响应
     */
    private String response;
    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 请求方法
     */
    private String method = "POST";

    /**
     * 原始类型 raw,form,json
     */
    private String req_body_type;
    /**
     * 请求form
     */
    private Set<YApiFormDTO> req_body_form;
    /**
     * 响应类型 json,raw
     */
    private String res_body_type = "json";

    /**
     * 描述
     */
    private String desc = "<h3>请补充描述</h3>";
    /**
     * 菜单
     */
    private String menu;
    /**
     * 菜单描述
     */
    private String menuDesc;

    /**
     * 请求路径参数
     */
    private Set<YApiPathVariableDTO> req_params;

    /**
     * 状态 done undone
     */
    private String status = "done";

    private boolean hasFile = false;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<YApiQueryDTO> getParams() {
        return params;
    }

    public void setParams(Set<YApiQueryDTO> params) {
        this.params = params;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRes_body_type() {
        return res_body_type;
    }

    public void setRes_body_type(String res_body_type) {
        this.res_body_type = res_body_type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Set<YApiHeaderDTO> getHeader() {
        return header;
    }

    public void setHeader(Set<YApiHeaderDTO> header) {
        this.header = header;
    }

    public String getReq_body_type() {
        return req_body_type;
    }

    public void setReq_body_type(String req_body_type) {
        this.req_body_type = req_body_type;
    }

    public Set<YApiFormDTO> getReq_body_form() {
        return req_body_form;
    }


    public Set<YApiPathVariableDTO> getReq_params() {
        return req_params;
    }

    public void setReq_params(Set<YApiPathVariableDTO> req_params) {
        this.req_params = req_params;
    }

    public void setReq_body_form(Set<YApiFormDTO> req_body_form) {
        this.req_body_form = req_body_form;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public boolean isHasFile() {
        return hasFile;
    }

    public void setHasFile(boolean hasFile) {
        this.hasFile = hasFile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public YApiDTO() {
    }


}
