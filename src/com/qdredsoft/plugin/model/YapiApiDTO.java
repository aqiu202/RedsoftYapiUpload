package com.qdredsoft.plugin.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * yapi dto
 *
 * @date 2019/2/11 3:16 PM
 */
public class YapiApiDTO implements Serializable {

    /**
     * 路径
     */
    private String path;
    /**
     * 请求参数
     */
    private List<YapiQueryDTO> params;
    /**
     * 头信息
     */
    private List<YapiHeaderDTO> header;
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
     * 请求 类型 raw,form,json
     */
    private String req_body_type;
    /**
     * 请求form
     */
    private Set<YapiFormDTO> req_body_form;
    /**
     * 响应类型 json,raw
     */
    private String res_body_type = "json";

    /**
     * 描述
     */
    private String desc;
    /**
     * 菜单
     */
    private String menu;
    /**
     * 菜单描述
     */
    private String menuDesc;

    /**
     * 请求参数
     */
    private List<YapiPathVariableDTO> req_params;

    private boolean hasFile = false;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<YapiQueryDTO> getParams() {
        return params;
    }

    public void setParams(List<YapiQueryDTO> params) {
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

    public List<YapiHeaderDTO> getHeader() {
        return header;
    }

    public void setHeader(List<YapiHeaderDTO> header) {
        this.header = header;
    }

    public String getReq_body_type() {
        return req_body_type;
    }

    public void setReq_body_type(String req_body_type) {
        this.req_body_type = req_body_type;
    }

    public Set<YapiFormDTO> getReq_body_form() {
        return req_body_form;
    }


    public List<YapiPathVariableDTO> getReq_params() {
        return req_params;
    }

    public void setReq_params(List<YapiPathVariableDTO> req_params) {
        this.req_params = req_params;
    }

    public void setReq_body_form(Set<YapiFormDTO> req_body_form) {
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

    public YapiApiDTO() {
    }


}
