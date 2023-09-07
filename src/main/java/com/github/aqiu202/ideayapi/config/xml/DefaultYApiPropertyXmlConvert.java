package com.github.aqiu202.ideayapi.config.xml;

import org.apache.commons.lang3.StringUtils;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DefaultYApiPropertyXmlConvert implements YApiPropertyXmlConvert<YApiProjectProperty> {

    private static final String KEY_URL = "url";
    private static final String KEY_PROJECT_ID = "project-id";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_DATA_MODE = "data-mode";
    private static final String KEY_STRATEGY = "property-naming-strategy";
    private static final String KEY_ENABLE_BASIC_SCOPE = "enable-basic-scope";
    private static final String KEY_ENABLE_TYPE_DESC = "enable-type-desc";
    private static final String KEY_USE_METHOD_DEFINE_AS_DESC = "use-method-define-as-desc";
    private static final String KEY_PASS_PAGE_URL = "pass-page-url";
    private static final String KEY_IGNORED_REQ_FIELDS = "ignored-req-fields";
    private static final String KEY_IGNORED_RES_FIELDS = "ignored-res-fields";

    @Override
    public Element serialize(@NotNull YApiProjectProperty property) {
        String url = property.getUrl();
        int projectId = property.getProjectId();
        String token = property.getToken();
        int strategy = property.getStrategy();
        int dataMode = property.getDataMode();
        boolean enableBasicScope = property.isEnableBasicScope();
        boolean typeDesc = property.isEnableTypeDesc();
        boolean useMethodDefineAsRemark = property.isUseMethodDefineAsRemark();
        boolean passPageUrl = property.isPassPageUrl();
        Element element = new Element("redsoft");
        if (StringUtils.isNotBlank(url)) {
            element.setAttribute(KEY_URL, url);
        }
        element.setAttribute(KEY_PROJECT_ID, Integer.toString(projectId));
        if (StringUtils.isNotBlank(token)) {
            element.setAttribute(KEY_TOKEN, token);
        }
        element.setAttribute(KEY_DATA_MODE, Integer.toString(dataMode));
        element.setAttribute(KEY_STRATEGY, Integer.toString(strategy));
        element.setAttribute(KEY_ENABLE_BASIC_SCOPE, Boolean.toString(enableBasicScope));
        element.setAttribute(KEY_ENABLE_TYPE_DESC, Boolean.toString(typeDesc));
        element.setAttribute(KEY_USE_METHOD_DEFINE_AS_DESC, Boolean.toString(useMethodDefineAsRemark));
        element.setAttribute(KEY_PASS_PAGE_URL, Boolean.toString(passPageUrl));
        String ignoredReqFields = property.getIgnoredReqFields();
        if (StringUtils.isBlank(ignoredReqFields)) {
            ignoredReqFields = "";
        }
        element.setAttribute(KEY_IGNORED_REQ_FIELDS, ignoredReqFields);
        String ignoredResFields = property.getIgnoredResFields();
        if (StringUtils.isBlank(ignoredResFields)) {
            ignoredResFields = "";
        }
        element.setAttribute(KEY_IGNORED_RES_FIELDS, ignoredResFields);
        return element;
    }

    @Override
    public YApiProjectProperty deserialize(@NotNull Element element) {
        YApiProjectProperty property = new YApiProjectProperty();
        String url = element.getAttributeValue(KEY_URL);
        if (StringUtils.isNotBlank(url)) {
            property.setUrl(url);
        }
        String p = element.getAttributeValue(KEY_PROJECT_ID);
        int projectId = 1;
        if (StringUtils.isNotBlank(p)) {
            projectId = Integer.parseInt(p);
        }
        property.setProjectId(projectId);
        String token = element.getAttributeValue(KEY_TOKEN);
        if (StringUtils.isNotBlank(token)) {
            property.setToken(token);
        }
        String s = element.getAttributeValue(KEY_STRATEGY);
        int strategy = 0;
        if (StringUtils.isNotBlank(s)) {
            strategy = Integer.parseInt(s);
        }
        property.setStrategy(strategy);
        String d = element.getAttributeValue(KEY_DATA_MODE);
        int dataMode = 0;
        if (StringUtils.isNotBlank(d)) {
            dataMode = Integer.parseInt(d);
        }
        property.setDataMode(dataMode);
        String enableBasicScope = element.getAttributeValue(KEY_ENABLE_BASIC_SCOPE);
        property.setEnableBasicScope(Boolean.parseBoolean(enableBasicScope));
        String typeDesc = element.getAttributeValue(KEY_ENABLE_TYPE_DESC);
        property.setEnableTypeDesc(Boolean.parseBoolean(typeDesc));
        String useMethodDefineAsRemark = element.getAttributeValue(KEY_USE_METHOD_DEFINE_AS_DESC);
        property.setUseMethodDefineAsRemark(Boolean.parseBoolean(useMethodDefineAsRemark));
        String passPageUrl = element.getAttributeValue(KEY_PASS_PAGE_URL);
        property.setPassPageUrl(Boolean.parseBoolean(passPageUrl));
        property.setIgnoredReqFields(element.getAttributeValue(KEY_IGNORED_REQ_FIELDS));
        property.setIgnoredResFields(element.getAttributeValue(KEY_IGNORED_RES_FIELDS));
        return property;
    }
}
