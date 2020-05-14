package com.redsoft.idea.plugin.yapiv2.xml;

import java.util.Objects;

public class YApiProjectProperty {

    private final static String DEFAULT_URL = "http://127.0.0.1:3000/";
    private final static int DEFAULT_PROJECT_ID = 1;
    private final static String DEFAULT_TOKEN = "";
    private final static int DEFAULT_STRATEGY = 0;

    private String url = DEFAULT_URL;
    private int projectId = DEFAULT_PROJECT_ID;
    private String token = DEFAULT_TOKEN;
    private int strategy = DEFAULT_STRATEGY;
    private boolean enableBasicScope;

    public YApiProjectProperty() {

    }

    public YApiProjectProperty(String url, int projectId, String token, int strategy,
            boolean enableBasicScope) {
        this.url = url;
        this.projectId = projectId;
        this.token = token;
        this.strategy = strategy;
        this.enableBasicScope = enableBasicScope;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public boolean isEnableBasicScope() {
        return enableBasicScope;
    }

    public void setEnableBasicScope(boolean enableBasicScope) {
        this.enableBasicScope = enableBasicScope;
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(this.url, this.projectId, this.token, this.strategy, this.enableBasicScope);
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
                Objects.equals(enableBasicScope, that.enableBasicScope);
    }
}
