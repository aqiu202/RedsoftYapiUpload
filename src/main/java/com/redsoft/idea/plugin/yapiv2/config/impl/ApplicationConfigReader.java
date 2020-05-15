package com.redsoft.idea.plugin.yapiv2.config.impl;

import com.redsoft.idea.plugin.yapiv2.config.ConfigurationReader;
import com.redsoft.idea.plugin.yapiv2.config.YApiApplicationPersistentState;
import com.redsoft.idea.plugin.yapiv2.xml.YApiApplicationProperty;
import com.redsoft.idea.plugin.yapiv2.xml.YApiPropertyConvertHolder;
import org.jdom.Element;

public class ApplicationConfigReader {

    private ApplicationConfigReader() {
    }

    private final static ConfigurationReader<YApiApplicationProperty> reader = () -> {
        Element element = YApiApplicationPersistentState.getInstance().getState();
        YApiApplicationProperty property = null;
        if (element != null) {
            property = YApiPropertyConvertHolder.getApplicationConvert().deserialize(element);
        }
        return property;
    };

    public static YApiApplicationProperty read() {
        return reader.read();
    }
}
