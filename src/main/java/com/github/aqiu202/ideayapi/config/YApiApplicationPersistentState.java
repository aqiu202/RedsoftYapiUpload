package com.github.aqiu202.ideayapi.config;

import com.github.aqiu202.ideayapi.config.xml.YApiApplicationProperty;
import com.github.aqiu202.ideayapi.config.xml.YApiPropertyConvertHolder;
import com.github.aqiu202.ideayapi.config.xml.YApiPropertyXmlConvert;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

@State(name = "ApplicationRedsoftYApiUpload", storages = @Storage("redsoft-yapi.xml"))
public class YApiApplicationPersistentState implements PersistentStateComponent<Element> {

    private final YApiPropertyXmlConvert<YApiApplicationProperty> convert = YApiPropertyConvertHolder
            .getApplicationConvert();
    private YApiApplicationProperty yApiApplicationProperty;

    YApiApplicationPersistentState() {
    }

    public static YApiApplicationPersistentState getInstance() {
        return ServiceManager.getService(YApiApplicationPersistentState.class);
    }

    @Override
    public Element getState() {
        return yApiApplicationProperty == null ? null
                : this.convert.serialize(yApiApplicationProperty);
    }

    @Override
    public void loadState(@NotNull Element element) {
        this.yApiApplicationProperty = this.convert.deserialize(element);
    }

}
