package com.redsoft.idea.plugin.yapi.xml;

import com.intellij.util.xmlb.annotations.Attribute;
import java.util.Objects;

public class YApiProperty {

    private final static String DEFAULT_URL = "http://127.0.0.1:3000/";
    private final static int DEFAULT_PROJECT_ID = 1;
    private final static String DEFAULT_TOKEN = "";

    private String url = DEFAULT_URL;
    private Integer projectId = DEFAULT_PROJECT_ID;
    private String token = DEFAULT_TOKEN;
    private boolean enableBasicScope;

    public YApiProperty() {

    }

    public YApiProperty(String url, Integer projectId, String token, boolean enableBasicScope) {
        this.url = url;
        this.projectId = projectId;
        this.token = token;
        this.enableBasicScope = enableBasicScope;
    }

    @Attribute
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Attribute
    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    @Attribute
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Attribute
    public boolean isEnableBasicScope() {
        return enableBasicScope;
    }

    public void setEnableBasicScope(boolean enableBasicScope) {
        this.enableBasicScope = enableBasicScope;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.url, this.projectId, this.token, this.enableBasicScope);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        YApiProperty that = (YApiProperty) obj;
        return Objects.equals(url, that.url) &&
                Objects.equals(projectId, that.projectId) &&
                Objects.equals(token, that.token) &&
                Objects.equals(enableBasicScope, that.enableBasicScope);
    }
}
