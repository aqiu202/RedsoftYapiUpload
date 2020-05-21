package com.redsoft.idea.plugin.yapiv2.model;

import com.redsoft.idea.plugin.yapiv2.base.ResultConvert;
import com.redsoft.idea.plugin.yapiv2.util.Builders;
import java.io.Serializable;
import java.util.Set;

/**
 * yapi dto
 *
 * @date 2019/2/11 3:16 PM
 */
@SuppressWarnings("unused")
public class YApiParam implements Serializable, ResultConvert<YApiSaveParam> {

    /**
     * 路径
     */
    private String path = "";
    /**
     * 头信息
     */
    private Set<YApiHeader> headers;
    /**
     * 请求Query参数
     */
    private Set<YApiQuery> params;
    /**
     * 请求form
     */
    private Set<YApiForm> req_body_form;
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
    private String req_body_type = "form";
    /**
     * 响应类型 json,raw
     */
    private String res_body_type = "json";

    /**
     * 描述
     */
    private String desc = "";
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
    private Set<YApiPathVariable> req_params;

    /**
     * 状态 done undone
     */
    private String status = "done";

    /**
     * 请求参数body 是否为json_schema
     */
    private boolean req_body_is_json_schema = true;
    /**
     * 响应参数body 是否为json_schema
     */
    private boolean res_body_is_json_schema = true;

    private boolean hasFile = false;

    private String consumes;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<YApiQuery> getParams() {
        return params;
    }

    public void setParams(Set<YApiQuery> params) {
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

    public Set<YApiHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(Set<YApiHeader> headers) {
        this.headers = headers;
    }

    public String getReq_body_type() {
        return req_body_type;
    }

    public void setReq_body_type(String req_body_type) {
        this.req_body_type = req_body_type;
    }

    public Set<YApiForm> getReq_body_form() {
        return req_body_form;
    }


    public Set<YApiPathVariable> getReq_params() {
        return req_params;
    }

    public void setReq_params(Set<YApiPathVariable> req_params) {
        this.req_params = req_params;
    }

    public void setReq_body_form(Set<YApiForm> req_body_form) {
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

    public boolean isReq_body_is_json_schema() {
        return req_body_is_json_schema;
    }

    public void setReq_body_is_json_schema(boolean req_body_is_json_schema) {
        this.req_body_is_json_schema = req_body_is_json_schema;
    }

    public boolean isRes_body_is_json_schema() {
        return res_body_is_json_schema;
    }

    public void setRes_body_is_json_schema(boolean res_body_is_json_schema) {
        this.res_body_is_json_schema = res_body_is_json_schema;
    }

    public String getConsumes() {
        return consumes;
    }

    public void setConsumes(String consumes) {
        this.consumes = consumes;
    }

    @Override
    public YApiSaveParam convert() {
        return Builders.of(YApiSaveParam::new)
                .with(YApiSaveParam::setMenu, this.menu)
                .with(YApiSaveParam::setMenuDesc, this.menuDesc)
                .with(YApiSaveParam::setPath, this.path)
                .with(YApiSaveParam::setTitle, this.title)
                .with(YApiSaveParam::setDesc, this.desc)
                .with(YApiSaveParam::setMethod, this.method)
                .with(YApiSaveParam::setStatus, this.status)
                .with(YApiSaveParam::setReq_query, this.params)
                .with(YApiSaveParam::setReq_params, this.req_params)
                .with(YApiSaveParam::setRes_body, this.response)
                .with(YApiSaveParam::setReq_headers, this.headers)
                .with(YApiSaveParam::setRes_body_type, this.res_body_type)
                .with(YApiSaveParam::setReq_body_type, this.req_body_type)
                .with(YApiSaveParam::setReq_body_form, this.req_body_form)
                .with(YApiSaveParam::setReq_body_other, this.requestBody)
                .with(YApiSaveParam::setReq_body_is_json_schema, this.req_body_is_json_schema)
                .with(YApiSaveParam::setRes_body_is_json_schema, this.res_body_is_json_schema)
                .with(YApiSaveParam::setReq_headers, this.headers)
                .build();
    }
}
