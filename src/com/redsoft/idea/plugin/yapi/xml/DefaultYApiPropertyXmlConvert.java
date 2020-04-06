package com.redsoft.idea.plugin.yapi.xml;

import com.jgoodies.common.base.Strings;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

public class DefaultYApiPropertyXmlConvert implements YApiPropertyXmlConvert<YApiProjectProperty> {

    private static final String KEY_URL = "url";
    private static final String KEY_PROJECT_ID = "project-id";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_STRATEGY = "property-naming-strategy";
    private static final String KEY_ENABLE_BASIC_SCOPE = "enable-basic-scope";

    @Override
    public Element serialize(@NotNull YApiProjectProperty property) {
        String url = property.getUrl();
        int projectId = property.getProjectId();
        String token = property.getToken();
        int strategy = property.getStrategy();
        boolean enableBasicScope = property.isEnableBasicScope();
        Element element = new Element("redsoft");
        if (Strings.isNotBlank(url)) {
            element.setAttribute(KEY_URL, url);
        }
        element.setAttribute(KEY_PROJECT_ID, Integer.toString(projectId));
        if (Strings.isNotBlank(token)) {
            element.setAttribute(KEY_TOKEN, token);
        }
        element.setAttribute(KEY_STRATEGY, Integer.toString(strategy));
        element.setAttribute(KEY_ENABLE_BASIC_SCOPE, Boolean.toString(enableBasicScope));
        return element;
    }

    @Override
    public YApiProjectProperty deserialize(@NotNull Element element) {
        YApiProjectProperty property = new YApiProjectProperty();
        String url = element.getAttributeValue(KEY_URL);
        if (Strings.isNotBlank(url)) {
            property.setUrl(url);
        }
        String p = element.getAttributeValue(KEY_PROJECT_ID);
        int projectId = 1;
        if (Strings.isNotBlank(p)) {
            projectId = Integer.parseInt(p);
        }
        property.setProjectId(projectId);
        String token = element.getAttributeValue(KEY_TOKEN);
        if (Strings.isNotBlank(token)) {
            property.setToken(token);
        }
        String s = element.getAttributeValue(KEY_STRATEGY);
        int strategy = 0;
        if (Strings.isNotBlank(s)) {
            strategy = Integer.parseInt(s);
        }
        property.setStrategy(strategy);
        String e = element.getAttributeValue(KEY_ENABLE_BASIC_SCOPE);
        boolean enableBasicScope = Boolean.parseBoolean(e);
        property.setEnableBasicScope(enableBasicScope);
        return property;
    }
}
