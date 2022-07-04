package com.github.aqiu202.ideayapi.config.impl;

import com.github.aqiu202.ideayapi.config.ConfigurationReader;
import com.github.aqiu202.ideayapi.config.YApiApplicationPersistentState;
import com.github.aqiu202.ideayapi.config.xml.YApiApplicationProperty;
import com.github.aqiu202.ideayapi.config.xml.YApiPropertyConvertHolder;
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
