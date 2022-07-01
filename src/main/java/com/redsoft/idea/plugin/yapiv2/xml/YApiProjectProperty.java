package com.redsoft.idea.plugin.yapiv2.xml;

import java.util.Objects;

public class YApiProjectProperty {

    private final static String DEFAULT_URL = "http://127.0.0.1:3000/";
    private final static int DEFAULT_PROJECT_ID = 1;
    private final static String DEFAULT_TOKEN = "";
    private final static int DEFAULT_STRATEGY = 0;
    private final static int DEFAULT_DATA_MODE = 0;

    private String url = DEFAULT_URL;
    private int projectId = DEFAULT_PROJECT_ID;
    private String token = DEFAULT_TOKEN;
    private int strategy = DEFAULT_STRATEGY;
    /**
     * 数据格式 0：json-schema（默认），1：json5
     */
    private int dataMode = DEFAULT_DATA_MODE;
    private boolean enableBasicScope;

    private boolean enableTypeDesc;

    public YApiProjectProperty() {

    }

    public YApiProjectProperty(String url, int projectId, String token, int strategy,
            int dataMode, boolean enableBasicScope, boolean enableTypeDesc) {
        this.projectId = projectId;
        this.token = token;
        this.strategy = strategy;
        this.dataMode = dataMode;
        this.enableBasicScope = enableBasicScope;
        this.enableTypeDesc = enableTypeDesc;
        this.setUrl(url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (url.matches(".*/+$")) {
            this.url = url.replaceAll("/+$", "");
        } else {
            this.url = url;
        }
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStrategy() {
        return strategy;
    }

    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }

    public int getDataMode() {
        return dataMode;
    }

    public void setDataMode(int dataMode) {
        this.dataMode = dataMode;
    }

    public boolean isEnableBasicScope() {
        return enableBasicScope;
    }

    public void setEnableBasicScope(boolean enableBasicScope) {
        this.enableBasicScope = enableBasicScope;
    }

    public boolean isEnableTypeDesc() {
        return enableTypeDesc;
    }

    public void setEnableTypeDesc(boolean enableTypeDesc) {
        this.enableTypeDesc = enableTypeDesc;
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(this.url, this.projectId, this.token, this.strategy, this.enableBasicScope, this.enableTypeDesc);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        YApiProjectProperty that = (YApiProjectProperty) obj;
        return Objects.equals(url, that.url) &&
                Objects.equals(projectId, that.projectId) &&
                Objects.equals(token, that.token) &&
                Objects.equals(strategy, that.strategy) &&
                Objects.equals(dataMode, that.dataMode) &&
                Objects.equals(enableBasicScope, that.enableBasicScope) &&
                Objects.equals(enableTypeDesc, that.enableTypeDesc);
    }
}
