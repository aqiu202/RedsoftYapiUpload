package com.redsoft.idea.plugin.yapiv2.model;

import com.redsoft.idea.plugin.yapiv2.base.ResultConvert;
import com.redsoft.idea.plugin.yapiv2.util.Builders;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * yapi接口信息
 *
 * @author aqiu 2019/2/11 3:16 PM
 */
public class YApiParam implements Serializable, ResultConvert<Collection<YApiSaveParam>> {

    /**
     * 路径
     */
    private Set<String> paths;
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
    private Set<String> methods;

    /**
     * 原始类型 raw,form,json
     */
    private String req_body_type = "json";
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

    public Set<String> getPaths() {
        return paths;
    }

    public void setPaths(Set<String> paths) {
        this.paths = paths;
    }

    public Set<String> getMethods() {
        return methods;
    }

    public void setMethods(Set<String> methods) {
        this.methods = methods;
    }

    @Override
    public Set<YApiSaveParam> convert() {
        Set<YApiSaveParam> result = new LinkedHashSet<>();
        for (String method : this.methods) {
            for (String path : this.paths) {
                result.add(Builders.of(YApiSaveParam::new)
                        .with(YApiSaveParam::setPath, path)
                        .with(YApiSaveParam::setMethod, method)
                        .with(YApiSaveParam::setMenu, this.menu)
                        .with(YApiSaveParam::setMenuDesc, this.menuDesc)
                        .with(YApiSaveParam::setTitle, this.title)
                        .with(YApiSaveParam::setDesc, this.desc)
                        .with(YApiSaveParam::setStatus, this.status)
                        .with(YApiSaveParam::setReq_query, this.params)
                        .with(YApiSaveParam::setReq_params, this.req_params)
                        .with(YApiSaveParam::setRes_body, this.response)
                        .with(YApiSaveParam::setReq_headers, this.headers)
                        .with(YApiSaveParam::setRes_body_type, this.res_body_type)
                        .with(YApiSaveParam::setReq_body_type, this.req_body_type)
                        .with(YApiSaveParam::setReq_body_form, this.req_body_form)
                        .with(YApiSaveParam::setReq_body_other, this.requestBody)
                        .with(YApiSaveParam::setReq_body_is_json_schema,
                                this.req_body_is_json_schema)
                        .with(YApiSaveParam::setRes_body_is_json_schema,
                                this.res_body_is_json_schema)
                        .with(YApiSaveParam::setReq_headers, this.headers)
                        .build());
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        YApiParam yApiParam = (YApiParam) o;
        return req_body_is_json_schema == yApiParam.req_body_is_json_schema
                && res_body_is_json_schema == yApiParam.res_body_is_json_schema
                && hasFile == yApiParam.hasFile && Objects.equals(paths, yApiParam.paths)
                && Objects.equals(headers, yApiParam.headers) && Objects.equals(
                params, yApiParam.params) && Objects.equals(req_body_form,
                yApiParam.req_body_form) && Objects.equals(title, yApiParam.title)
                && Objects.equals(response, yApiParam.response) && Objects.equals(
                requestBody, yApiParam.requestBody) && Objects.equals(methods,
                yApiParam.methods) && Objects.equals(req_body_type, yApiParam.req_body_type)
                && Objects.equals(res_body_type, yApiParam.res_body_type)
                && Objects.equals(desc, yApiParam.desc) && Objects.equals(menu,
                yApiParam.menu) && Objects.equals(menuDesc, yApiParam.menuDesc)
                && Objects.equals(req_params, yApiParam.req_params)
                && Objects.equals(status, yApiParam.status) && Objects.equals(
                consumes, yApiParam.consumes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paths, headers, params, req_body_form, title, response, requestBody,
                methods, req_body_type, res_body_type, desc, menu, menuDesc, req_params, status,
                req_body_is_json_schema, res_body_is_json_schema, hasFile, consumes);
    }
}
