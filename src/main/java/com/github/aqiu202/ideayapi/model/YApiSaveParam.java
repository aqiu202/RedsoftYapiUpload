package com.github.aqiu202.ideayapi.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * yapi 保存请求参数
 *
 * @author aqiu 2019/1/31 11:43 AM
 */
@SuppressWarnings("unused")
public class YApiSaveParam implements Serializable {

    /**
     * 项目 token  唯一标识
     */
    private String token;

    /**
     * 请求参数
     */
    private Set<YApiQuery> req_query;
    /**
     * header
     */
    private Set<YApiHeader> req_headers;
    /**
     * 请求参数 form 类型
     */
    private Set<YApiForm> req_body_form;
    /**
     * 标题
     */
    private String title;
    /**
     * 分类id
     */
    private String catid;
    /**
     * 请求数据类型   raw,form,json
     */
    private String req_body_type = "json";
    /**
     * 请求数据body
     */
    private String req_body_other;

    /**
     * 路径
     */
    private String path;
    /**
     * 状态 undone,默认done
     */
    private String status = "done";
    /**
     * 返回参数类型  json,raw
     */
    private String res_body_type = "json";

    /**
     * 返回参数
     */
    private String res_body;
    /**
     * 请求参数body 是否为json_schema
     */
    private boolean req_body_is_json_schema = true;
    /**
     * 返回参数是否为json_schema
     */
    private boolean res_body_is_json_schema = true;

    /**
     * 创建的用户名
     */
    private Integer edit_uid = 11;
    /**
     * 用户名称
     */
    private String username;

    /**
     * 邮件开关
     */
    private boolean switch_notice;

    private String message = " ";
    /**
     * 文档描述
     */
    private String desc = "<h3>请补充描述</h3>";

    /**
     * 请求方式
     */
    private String method = "POST";
    /**
     * 请求参数
     */
    private Set<YApiPathVariable> req_params;


    private String id;

    /**
     * 菜单名称
     */
    private String menu;
    /**
     * 菜单描述
     */
    private String menuDesc;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<YApiQuery> getReq_query() {
        return req_query;
    }

    public void setReq_query(Set<YApiQuery> req_query) {
        this.req_query = req_query;
    }

    public Set<YApiHeader> getReq_headers() {
        return req_headers;
    }

    public void setReq_headers(Set<YApiHeader> req_headers) {
        this.req_headers = req_headers;
    }

    public Set<YApiForm> getReq_body_form() {
        return req_body_form;
    }

    public void setReq_body_form(Set<YApiForm> req_body_form) {
        this.req_body_form = req_body_form;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRes_body_type() {
        return res_body_type;
    }

    public void setRes_body_type(String res_body_type) {
        this.res_body_type = res_body_type;
    }

    public String getRes_body() {
        return res_body;
    }

    public void setRes_body(String res_body) {
        this.res_body = res_body;
    }

    public boolean isSwitch_notice() {
        return switch_notice;
    }

    public void setSwitch_notice(boolean switch_notice) {
        this.switch_notice = switch_notice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Set<YApiPathVariable> getReq_params() {
        return req_params;
    }

    public void setReq_params(Set<YApiPathVariable> req_params) {
        this.req_params = req_params;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReq_body_type() {
        return req_body_type;
    }

    public void setReq_body_type(String req_body_type) {
        this.req_body_type = req_body_type;
    }

    public String getReq_body_other() {
        return req_body_other;
    }

    public void setReq_body_other(String req_body_other) {
        this.req_body_other = req_body_other;
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

    public Integer getEdit_uid() {
        return edit_uid;
    }

    public void setEdit_uid(Integer edit_uid) {
        this.edit_uid = edit_uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public YApiSaveParam() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        YApiSaveParam that = (YApiSaveParam) o;
        return req_body_is_json_schema == that.req_body_is_json_schema
                && res_body_is_json_schema == that.res_body_is_json_schema
                && switch_notice == that.switch_notice && Objects.equals(token, that.token)
                && Objects.equals(req_query, that.req_query) && Objects.equals(
                req_headers, that.req_headers) && Objects.equals(req_body_form,
                that.req_body_form) && Objects.equals(title, that.title)
                && Objects.equals(catid, that.catid) && Objects.equals(
                req_body_type, that.req_body_type) && Objects.equals(req_body_other,
                that.req_body_other) && Objects.equals(path, that.path)
                && Objects.equals(status, that.status) && Objects.equals(
                res_body_type, that.res_body_type) && Objects.equals(res_body, that.res_body)
                && Objects.equals(edit_uid, that.edit_uid) && Objects.equals(
                username, that.username) && Objects.equals(message, that.message)
                && Objects.equals(desc, that.desc) && Objects.equals(method,
                that.method) && Objects.equals(req_params, that.req_params)
                && Objects.equals(id, that.id) && Objects.equals(menu, that.menu)
                && Objects.equals(menuDesc, that.menuDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, req_query, req_headers, req_body_form, title, catid,
                req_body_type,
                req_body_other, path, status, res_body_type, res_body, req_body_is_json_schema,
                res_body_is_json_schema, edit_uid, username, switch_notice, message, desc, method,
                req_params, id, menu, menuDesc);
    }
}
